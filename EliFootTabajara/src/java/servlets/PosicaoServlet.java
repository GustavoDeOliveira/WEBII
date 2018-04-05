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
import modelo.Posicao;
import org.eclipse.persistence.internal.jpa.EntityManagerFactoryProvider;
import persistencia.JogadorDAO;
import persistencia.PosicaoDAO;
import persistencia.TimeDAO;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author gustavo
 */
@WebServlet(name = "PosicaoServlet", urlPatterns = {"/PosicaoServlet"})
public class PosicaoServlet extends HttpServlet {

    private static final PosicaoDAO PDAO = new PosicaoDAO(Persistence.createEntityManagerFactory("EliFootTabajaraPU"));
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
        request.setAttribute("posicoes", PDAO.findPosicaoEntities());
        RequestDispatcher rd = request.getRequestDispatcher("/Posicoes/Index.jsp");
        rd.forward(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idGet = request.getParameter("id");
        int id = idGet == null || idGet.equals("") ? 0 : Integer.parseInt(request.getParameter("id"));
        Posicao posicao = PDAO.findPosicao(id);
        request.setAttribute("posicao", posicao);
        RequestDispatcher rd = request.getRequestDispatcher("/Posicoes/Posicao.jsp");
        rd.forward(request, response);
    }
    
    private void salvar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String descricao = request.getParameter("descricao");
        String idGet = request.getParameter("id");
        Posicao t = new Posicao();
        t.setDescricao(descricao);
        if (idGet == null || idGet.equals(""))
            PDAO.create(t);
        else
            try {
                t.setId(Integer.parseInt(idGet));
                PDAO.edit(t);
            } catch (Exception ex) {
                log("Erro editando posicao.", ex);
            }
        RequestDispatcher rd = request.getRequestDispatcher("./PosicaoServlet?acao=listar");
        rd.forward(request, response);
    }
    
    private void excluir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String[] idsGet = request.getParameterValues("ids");
            Integer[] ids = new Integer[idsGet.length];
            StringBuilder sb = new StringBuilder("DELETE FROM Posicao t WHERE t.id IN (");
            for (int i = 0; i < ids.length; i++) {
                ids[i] = Integer.parseInt(idsGet[i]);
                sb.append(":id").append(i);
                if (i + 1 < ids.length) {
                    sb.append(", ");
                } else {
                    sb.append(")");
                }
            }
        EntityManager em = PDAO.getEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery(sb.toString());
            for (int i = 0; i < ids.length; i++) {
                query.setParameter("id" + i, ids[i]);
            }
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            log("Erro ao excluir posicao.", e);
        } finally {
            if (em != null) {
                em.close();
            }
            RequestDispatcher rd = request.getRequestDispatcher("./PosicaoServlet?acao=listar");
            rd.forward(request, response);
        }
        
    }
    
}
