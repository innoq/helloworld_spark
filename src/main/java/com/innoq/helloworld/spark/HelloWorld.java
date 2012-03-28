package com.innoq.helloworld.spark;

import static spark.Spark.*;

import org.antlr.stringtemplate.StringTemplate;
import spark.*;

import org.stringtemplate.v4.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.beans.Beans;
import java.util.Set;

public class HelloWorld {

    public static void main(String[] args) {
        final STGroup layoutGroup =
                new STGroupFile("./src/main/resources/views/layout.stg");
        final ST layout = layoutGroup.getInstanceOf("layout");
        final EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("defaultPU");



        get(new Route("/") {
            @Override
            public Object handle(Request request, Response response) {
                STGroup.trackCreationEvents = true;
                STGroup g = new STGroupFile("./src/main/resources/views/dashboard.stg");
                Set<String> l = g.getTemplateNames();
                ST dashboardTemplate = g.getInstanceOf("dashboard");
                Dashboard db = new Dashboard(emf.createEntityManager());
                db.load();
                dashboardTemplate.add("profile", db.getProfile());
                dashboardTemplate.add("statuses", db.getStatuses());
                dashboardTemplate.add("unaccepted_contacts", db.getUnacceptedContacts());
                dashboardTemplate.add("contacts_of_contacts", db.getContactsOfContacts());
                return render(layout, dashboardTemplate);
            }
        });
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

    private static Object render(ST layout, ST template) {
        return render(layout, template.render());
    }

    private static String render(ST layout, String body) {
        layout.remove("body");
        layout.add("body", body);
        return layout.render();
    }

}