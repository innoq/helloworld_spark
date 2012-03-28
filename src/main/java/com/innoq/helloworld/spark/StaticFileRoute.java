package com.innoq.helloworld.spark;

import spark.Request;
import spark.Response;
import spark.Route;

import java.io.*;
import java.nio.channels.FileChannel;

// World's ugliest hack. Don't use.
public class StaticFileRoute extends Route {
    public StaticFileRoute(String directory) {
        super(directory + "/*");
    }

    @Override
    public Object handle(Request request, Response response) {
        System.out.println("Pathinfo: " + request.pathInfo());
        try {
            OutputStream out = response.raw().getOutputStream();
            InputStream in = new FileInputStream("./public/" + request.pathInfo());
            int count;
            byte[] buffer = new byte[8192];
            while ((count = in.read(buffer)) > 0)
                out.write(buffer, 0, count);
            out.flush();
            halt(200);
        } catch (IOException e) {
            halt(500, e.getMessage());
        } finally {
            return "";
        }

    }
}
