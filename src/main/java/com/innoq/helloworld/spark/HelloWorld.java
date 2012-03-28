package com.innoq.helloworld.spark;

import static spark.Spark.*;

import spark.*;

import org.stringtemplate.v4.*;

public class HelloWorld {

    public static void main(String[] args) {
        final STGroup layoutGroup =
                new STGroupFile("./src/main/resources/views/layout.stg");
        final ST layout = layoutGroup.getInstanceOf("layout");


        get(new Route("/layout") {
            @Override
            public Object handle(Request request, Response response) {
                return render(layout, "");
            }
        });

        before(new Filter() {
            @Override
            public void handle(Request request, Response response) {
                layoutGroup.unload();
            }
        });


        get(new Route("/info") {
            @Override
            public Object handle(Request request, Response response) {
                ST status = layoutGroup.getInstanceOf("status");

                for (String header : request.headers()) {
                    status.add("header", header);
                    status.add(header, request.headers(header));
                }
                response.type("text/html");
                return status.toString();
            }
        });

        get(new StaticFileRoute("/javascripts"));
        get(new StaticFileRoute("/stylesheets"));
        get(new StaticFileRoute("/images"));

    }

    private static String render(ST layout, String body) {
        layout.add("body", body);
        return layout.render();
    }

}