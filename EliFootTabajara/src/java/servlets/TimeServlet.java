/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
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
import persistencia.JogadorDAO;
import persistencia.TecnicoDAO;
import persistencia.TimeDAO;

/**
 *
 * @author gustavo
 */
@WebServlet(name = "TimeServlet", urlPatterns = {"/TimeServlet"})
public class TimeServlet extends HttpServlet {

    private static final TimeDAO EDAO = new TimeDAO(Persistence.createEntityManagerFactory("EliFootTabajaraPU"));
    private static final JogadorDAO JDAO = new JogadorDAO(Persistence.createEntityManagerFactory("EliFootTabajaraPU"));
    private static final TecnicoDAO TDAO = new TecnicoDAO(Persistence.createEntityManagerFactory("EliFootTabajaraPU"));

    
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
        request.setAttribute("times", EDAO.findTimeEntities());
        RequestDispatcher rd = request.getRequestDispatcher("/Times/Index.jsp");
        rd.forward(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = request.getParameter("id") != null ? Integer.parseInt(request.getParameter("id")) : 0;
        Equipe time = EDAO.findEquipe(id);
        request.setAttribute("time", time);
        request.setAttribute("tecnicos", TDAO.findTecnicoEntities());
        RequestDispatcher rd = request.getRequestDispatcher("/Times/Time.jsp");
        rd.forward(request, response);
    }
    
    private void salvar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idGet = request.getParameter("id");
        String nome = request.getParameter("nome");
        String tecnicoIdGet = request.getParameter("tecnico_id");
        Equipe t = new Equipe();
        t.setNome(nome);
        if (tecnicoIdGet != null && !tecnicoIdGet.equals("")) {
            t.setTecnico(TDAO.findTecnico(Integer.parseInt(tecnicoIdGet)));
        }
        try {
            if (idGet == null || idGet.equals("")) {
                EDAO.create(t);
            } else {
                Integer id = Integer.parseInt(request.getParameter("id"));
                t.setId(id);
                EDAO.edit(t);
            }
        } catch (Exception e) {
            log("Erro editando time.", e);
        }
        RequestDispatcher rd = request.getRequestDispatcher("./TimeServlet?acao=listar");
        rd.forward(request, response);
    }
    
    private void excluir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] idsReq = request.getParameterValues("ids");
        int[] ids = new int[idsReq.length];
        StringBuilder sb = new StringBuilder("DELETE FROM Time t WHERE t.id IN (");
        for (int i = 0; i < ids.length; i++) {
            ids[i] = Integer.parseInt(idsReq[i]);
            sb.append(":id").append(i);
            sb.append(i + 1 < ids.length ? ", " : ")");
        }
        EntityManager em = EDAO.getEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery(sb.toString());
            for (int i = 0; i < ids.length; i++) {
                query.setParameter("id" + i, ids[i]);
            }
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            log("Erro excluindo times.", e);
        } finally {
            if (em != null) {
                em.close();
            }
            RequestDispatcher rd = request.getRequestDispatcher("./TimeServlet?acao=listar");
            rd.forward(request, response);
        }
        
    }

}
