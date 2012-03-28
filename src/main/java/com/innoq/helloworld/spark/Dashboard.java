package com.innoq.sparktest;

import static spark.Spark.*;
import spark.*;
import org.antlr.stringtemplate.*;
import org.antlr.stringtemplate.language.*;
 
public class HelloWorld {

   public static void main(String[] args) {
       final StringTemplateGroup group = 
	   new StringTemplateGroup("myGroup", "./src/main/resources/views",
					 DefaultTemplateLexer.class);
      
      get(new Route("/info") {
         @Override
         public Object handle(Request request, Response response) {
	     StringTemplate status = group.getInstanceOf("status");
	     
	     for (String header : request.headers()) {
		 status.setAttribute("header", header);
		 status.setAttribute(header, request.headers(header));
	     } 	     
	     response.type("text/html");
	     return status.toString();
         }
      });

      get(new Route("/hello") {
         @Override
         public Object handle(Request request, Response response) {
	     StringTemplate hello = new StringTemplate("Hello, $name$", DefaultTemplateLexer.class);
	     hello.setAttribute("name", "World");
	     return hello.toString();
         }
      });

      get(new Route("/home") {
         @Override
         public Object handle(Request request, Response response) {
	     StringTemplate helloAgain = group.getInstanceOf("homepage");
	     
	     helloAgain.setAttribute("title", "Welcome To StringTemplate");
	     helloAgain.setAttribute("name", "World");
	     helloAgain.setAttribute("friends", "Ter");
	     helloAgain.setAttribute("friends", "Kunle");
	     helloAgain.setAttribute("friends", "Micheal");
	     helloAgain.setAttribute("friends", "Marq");
 	     
	     return helloAgain.toString();
         }
      });

      
   }

}