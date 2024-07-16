package com.cac.proyecto;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/buscarPelicula")
public class BuscarPeliculaControlador extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Obtener el ID de la película a buscar desde el parámetro de la solicitud
		String idParam = request.getParameter("id");

		if (idParam == null || idParam.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		int idPelicula;
		try {
			idPelicula = Integer.parseInt(idParam);
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		// Obtener la conexión a la base de datos
		Conexion conexion = new Conexion();
		Connection conn = conexion.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			// Consulta SQL para buscar la película por su ID
			String sql = "SELECT * FROM peliculas WHERE id_pelicula = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, idPelicula);

			// Ejecutar la consulta de búsqueda en la base de datos
			rs = stmt.executeQuery();

			if (rs.next()) {
				// Si se encontró la película, obtener sus datos y enviar como respuesta
				String titulo = rs.getString("titulo");
				String genero = rs.getString("genero");
				String duracion = rs.getString("duracion");
				String imagen = rs.getString("imagen");

				StringBuilder jsonRespuesta = new StringBuilder();
				jsonRespuesta.append("{");
				jsonRespuesta.append("\"id\": ").append(idPelicula).append(", ");
				jsonRespuesta.append("\"titulo\": \"").append(titulo).append("\", ");
				jsonRespuesta.append("\"genero\": \"").append(genero).append("\", ");
				jsonRespuesta.append("\"duracion\": \"").append(duracion).append("\", ");
				jsonRespuesta.append("\"imagen\": \"").append(imagen).append("\"");
				jsonRespuesta.append("}");

				response.setContentType("application/json");
				response.getWriter().write(jsonRespuesta.toString());
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			// Cerrar la conexión y liberar recursos
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}