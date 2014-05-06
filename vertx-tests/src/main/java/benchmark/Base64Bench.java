package benchmark;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.json.impl.Base64;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Parameter;
import com.martiansoftware.jsap.SimpleJSAP;
import com.martiansoftware.jsap.Switch;

import de.jpaw.vertx.lib.ByteBuilder;

// base64 encoding shootout, Java 8 vs external implementation.

// result (on my machine)
//[cobol@booster tmp]$ java -jar bench.jar -i java8
//22:17:58.905 [main] INFO  benchmark.Base64Bench - Did 400000 runs in 243 ms, that's 1646 records per millisecond
//[cobol@booster tmp]$ java -jar bench.jar -i ext
//22:18:07.868 [main] INFO  benchmark.Base64Bench - Did 400000 runs in 3139 ms, that's 127 records per millisecond
//[cobol@booster tmp]$ java -jar bench.jar -i java8s
//22:33:23.743 [main] INFO  benchmark.Base64Bench - Did 400000 runs in 380 ms, that's 1052 records per millisecond
//
// => Java 8 native implementation to byte [] wins for encoding by a factor of 13 over the external, and by 1.6 over the toString

public class Base64Bench {
    private static final Logger LOG = LoggerFactory.getLogger(Base64Bench.class);
    private static final String DATA = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."; 
    private static final byte [] BYTES = DATA.getBytes(); 

	public static void main(String[] args) {
        SimpleJSAP commandLineOptions = null;
		try {
			commandLineOptions = new SimpleJSAP("Base64 benchmark", "runs different implementations of base64 encode / decode", new Parameter[] {});
			commandLineOptions.registerParameter(new FlaggedOption("impl", JSAP.STRING_PARSER, null, JSAP.NOT_REQUIRED, 'i', "impl", "base64 implementation (java8, ext)"));
			commandLineOptions.registerParameter(new Switch("decode", 'd', "decode", "decode instead of encode"));
			commandLineOptions.registerParameter(new FlaggedOption("runs", JSAP.INTEGER_PARSER, null, JSAP.NOT_REQUIRED, 'n', "nruns", "number of iterations"));
		} catch (JSAPException e) {
			LOG.error("Cannot create command line parameters: {}", e);
			System.exit(1);
		}
		// add input / output related options
		
        JSAPResult params = commandLineOptions.parse(args);
        if (commandLineOptions.messagePrinted()) {
            System.err.println("(use option --help for usage)");
            System.exit(1);
        }

        
        int nruns = params.getInt("runs", 400000);
    	java.util.Base64.Encoder j8encoder = java.util.Base64.getEncoder();
        
        Date start = new Date();
        switch (params.getString("impl", "java8")) {
        case "java8":
        	for (int i = 0; i < nruns; ++i) {
        		byte [] result = j8encoder.encode(BYTES);
        	}
        	break;
        case "java8s":
        	for (int i = 0; i < nruns; ++i) {
        		String result = j8encoder.encodeToString(BYTES);
        	}
        	break;
        case "ext":
        	for (int i = 0; i < nruns; ++i) {
        		ByteBuilder target = new ByteBuilder();
        		de.jpaw.vertx.lib.Base64.encodeToByte(target, BYTES, 0, BYTES.length);
        	}
        	break;
        case "vertx":
        	// org.vertx.java.core.json.impl.Base64;
        	;
        }
        Date end = new Date();
        
        long millis = end.getTime() - start.getTime();
		LOG.info("Did {} runs in {} ms, that's {} records per millisecond",
				nruns, millis, nruns / millis);
	}

}
