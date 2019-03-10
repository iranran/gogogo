package hello.work;

import io.grpc.Deadline;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ProtoServiceGent {

    public static void genStub(List<String> lines) throws Exception{
        String service = lines.stream().filter(l -> l.trim().startsWith("service")).findFirst().get();
        service = service.substring(7, service.length()-1);
        //System.out.println(service);

        StringBuilder builder = new StringBuilder();
        builder.append("    @Autowired\n" +
                "    @Qualifier(\"brokerServerChannel\")\n" +
                "    private Channel brokerServerChannel;\n");
        builder.append("    private ").append(service).append("Grpc.")
                .append(service.trim()).append("BlockingStub getStub() {\n")
                .append("       return ").append(service).append("Grpc.newBlockingStub(brokerServerChannel)\n")
                .append("           .withDeadline(Deadline.after(GrpcConstant.DURATION_10_SECONDS, GrpcConstant.TIME_UNIT))")
                .append(";").append("\n")
                .append("   }\n");
        System.out.println(builder);
    }
    public static void genMethods(List<String> lines) throws Exception{
        lines.stream().filter(line -> line.trim().startsWith("rpc"))
                .map(line -> line.trim())
                .forEach(line -> {
                    //System.out.println(line);
                    int requsetStartIndex = line.indexOf("(");
                    int requsetEndIndex = line.indexOf(")") ;
                    String request = (line.substring(requsetStartIndex, requsetEndIndex) + " request)" );
                    String method = line.substring(3, requsetStartIndex).trim();
                    method = method.substring(0,1).toLowerCase() + method.substring(1);


                    int responseStartIndex = line.lastIndexOf("(") + 1;
                    int responseEndIndex = line.lastIndexOf(")");
                    String response = line.substring(responseStartIndex,responseEndIndex);

                    StringBuilder builder = new StringBuilder();
                    builder.append("    public ").append(response).append(" ")
                            .append(method.trim()).append(request);
                    builder.append(" {").append("\n");
                    builder.append("       ").append(response.trim()).append(" response ").append(" = getStub().").append(method.trim())
                            .append("(request)").append(";").append("\n");
                    builder.append("        return response;").append("\n");
                    builder.append("    }").append("\n");
                    System.out.println(builder.toString());
                });
    }


    public static void main(String[] args) throws Exception{
        String classname = "CpalOrderClient";
        String filename = "/Users/liwei/sourcecode/cpalworkspace/proto/src/main/proto/capital/order.proto";
        List<String> lines = FileUtils.readLines(new File(filename));



        String pack = lines.stream().filter(l -> l.trim().contains("java_package")).findFirst().get();
        pack = (pack.split("=")[1].trim().replaceAll("\"", "").replaceAll(";", ""));

        String importstring = "\nimport "+pack + ".*;\n" +
                "import io.bhex.hive.platform.constants.GrpcConstant;\n" +
                "import io.grpc.Channel;\n" +
                "import io.grpc.Deadline;\n" +
                "import lombok.extern.slf4j.Slf4j;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.beans.factory.annotation.Qualifier;\n" +
                "import org.springframework.stereotype.Component;\n";
        StringBuilder builder = new StringBuilder();
        builder.append(importstring).append("\n");
        builder.append("@Slf4j\n" +
                "@Component\n" +
                "public class ").append(classname).append(" {\n");

        System.out.println(builder);

        genStub(lines);

        genMethods(lines);
        System.out.println("\n}");
    }
}

