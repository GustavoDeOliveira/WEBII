package servlets;

/**
 *
 * @author gustavo
 */
public class Controller {
    public String getActionLink(String acao, String controller, String parametros) {
        return "/" + controller + "Servlet?acao=" + acao + "&" + parametros;
    }
}
