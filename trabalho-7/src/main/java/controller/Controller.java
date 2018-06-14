package controller;

import spark.Request;
import spark.Response;

public abstract class Controller {
    
    protected Request request;
    protected Response response;

    public Controller(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    public Controller() {
        this.request = null;
        this.response = null;
    }
    
    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
