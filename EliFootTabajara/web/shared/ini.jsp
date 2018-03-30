<%-- 
    Document   : ini
    Created on : 10/03/2018, 15:02:23
    Author     : gustavo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>EliFoot Tabajara Admin</title>

        <meta name="description" content="Source code generated using layoutit.com">
        <meta name="author" content="LayoutIt!">

        <link href="/EliFootTabajara/css/bootstrap.min.css" rel="stylesheet">
        <link href="/EliFootTabajara/css/style.css" rel="stylesheet">
        <link href="/EliFootTabajara/css/themes.min.css" rel="stylesheet">
        
        <style type="text/css">
            .background{
                background-color: #333333;
            }
        </style>

    </head>
    <body class="theme-dust background">

        <div class="container-fluid">
            <div class="row">
                <div class="col-md-8 col-md-offset-2 col-sm-10 col-sm-offset-1">
                    <nav class="navbar navbar-inverse" role="navigation">
                        <div class="navbar-inner">
                            <div class="navbar-header">
                                <a class="navbar-brand" href="/EliFootTabajara/Index.jsp">EliFoot</a>
                            </div>
                            <ul class="nav navbar-nav nav-pills">
                                <li>
                                    <a href="/EliFootTabajara/JogadorServlet?acao=listar">Jogadores</a>
                                </li>
                                <li>
                                    <a href="/EliFootTabajara/TimeServlet?acao=listar">Times</a>
                                </li>
                            </ul>
                        </div>
                    </nav>
                </div>
            </div>
            <div class="row">
                <div class="col-md-8 col-md-offset-2 col-sm-10 col-sm-offset-1">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">
                            </h3>
                            <h3 class="text-center text-default">
                                Painel de Administrador do EliFoot Tabajara
                            </h3>
                        </div>
                        <div class="panel-body">