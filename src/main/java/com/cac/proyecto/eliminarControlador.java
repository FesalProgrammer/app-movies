package com.cac.proyecto;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
@WebServlet("/eliminar")
public class eliminarControlador extends HttpServlet {
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "*");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		Conexion conexion = new Conexion();
		Connection conn = conexion.getConnection();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			String query = "DELETE FROM peliculas WHERE id_pelicula = ?";
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setInt(1, id);
			int rowsDeleted = statement.executeUpdate();
			if (rowsDeleted > 0) {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			conexion.close();
		}
	}







	}
