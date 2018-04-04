/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Jogador;
import modelo.Equipe;
import modelo.Tecnico;
import org.eclipse.persistence.internal.jpa.EntityManagerFactoryProvider;
import persistencia.JogadorDAO;
import persistencia.TecnicoDAO;
import persistencia.TimeDAO;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author gustavo
 */
@WebServlet(name = "TecnicoServlet", urlPatterns = {"/TecnicoServlet"})
public class TecnicoServlet extends HttpServlet {

    private static final TecnicoDAO TDAO = new TecnicoDAO(Persistence.createEntityManagerFactory("EliFootTabajaraPU"));
    private static final TimeDAO EDAO = new TimeDAO(Persistence.createEntityManagerFactory("EliFootTabajaraPU"));
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");
        switch(acao) {
            case "listar":
                listar(request, response);
                break;
            case "editar":
                editar(request, response);
                break;
            case "salvar":
                salvar(request, response);
                break;
            case "excluir":
                excluir(request, response);
                break;
            default:
                throw new ServletException("Ação '" + acao + "' inválida.");
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("tecnicos", TDAO.findTecnicoEntities());
        RequestDispatcher rd = request.getRequestDispatcher("/Tecnicos/Index.jsp");
        rd.forward(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idGet = request.getParameter("id");
        int id = idGet == null || idGet.equals("") ? 0 : Integer.parseInt(request.getParameter("id"));
        Tecnico tecnico = TDAO.findTecnico(id);
        request.setAttribute("tecnico", tecnico);
        RequestDispatcher rd = request.getRequestDispatcher("/Tecnicos/Tecnico.jsp");
        rd.forward(request, response);
    }
    
    private void salvar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String idGet = request.getParameter("id");
        Tecnico t = new Tecnico();
        t.setNome(nome);
        if (idGet == null || idGet.equals(""))
            TDAO.create(t);
        else
            try {
                t.setId(Integer.parseInt(idGet));
                TDAO.edit(t);
            } catch (Exception ex) {
                log("Erro editando jogador.", ex);
            }
        RequestDispatcher rd = request.getRequestDispatcher("./TecnicoServlet?acao=listar");
        rd.forward(request, response);
    }
    
    private void excluir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String[] idsGet = request.getParameterValues("ids");
            Integer[] ids = new Integer[idsGet.length];
            StringBuilder sb = new StringBuilder("DELETE FROM Tecnico t WHERE t.id IN (");
            for (int i = 0; i < ids.length; i++) {
                ids[i] = Integer.parseInt(idsGet[i]);
                sb.append(":id").append(i);
                if (i + 1 < ids.length) {
                    sb.append(", ");
                } else {
                    sb.append(")");
                }
            }
        EntityManager em = TDAO.getEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery(sb.toString());
            for (int i = 0; i < ids.length; i++) {
                query.setParameter("id" + i, ids[i]);
            }
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            log("Erro ao excluir tecnico.", e);
        } finally {
            if (em != null) {
                em.close();
            }
            RequestDispatcher rd = request.getRequestDispatcher("./TecnicoServlet?acao=listar");
            rd.forward(request, response);
        }
        
    }
    
}
