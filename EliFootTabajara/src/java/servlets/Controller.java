package servlets;

/**
 *
 * @author gustavo
 */
public class Controller {
    public static String getActionLink(String acao, String servlet, String parametros) {
        return "/" + servlet + "Servlet?acao=" + acao + "&" + parametros;
    }
}
