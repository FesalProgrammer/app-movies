package com.cac.proyecto;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/peliculas/actualizar")
public class ActualizarControlador extends HttpServlet {
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Configurar cabeceras CORS
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "*");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");

		Conexion conexion = new Conexion();
		Connection conn = conexion.getConnection();

		try {
			ObjectMapper mapper = new ObjectMapper();
			Pelicula pelicula = mapper.readValue(request.getInputStream(), Pelicula.class);

			String query = "UPDATE peliculas SET titulo = ?, genero = ?, duracion = ?, imagen = ? WHERE id_pelicula = ?";
			PreparedStatement statement = conn.prepareStatement(query);

			statement.setString(1, pelicula.getTitulo());
			statement.setString(2, pelicula.getGenero());
			statement.setString(3, pelicula.getDuracion());
			statement.setString(4, pelicula.getImagen());
			statement.setInt(5, pelicula.getIdPelicula());

			int rowsUpdated = statement.executeUpdate();

			if (rowsUpdated > 0) {
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			conexion.close();
		}
	}
}
