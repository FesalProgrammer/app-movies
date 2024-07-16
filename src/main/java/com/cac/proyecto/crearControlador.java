package com.cac.proyecto;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
@WebServlet("/insertar")
public class crearControlador extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Configurar cabeceras CORS
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "*");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		Conexion conexion = new Conexion();
		Connection conn = conexion.getConnection();
		try {
			ObjectMapper mapper = new ObjectMapper();
			Pelicula pelicula = mapper.readValue(request.getInputStream(), Pelicula.class);
			// Consulta SQL para insertar una nueva película en la tabla 'peliculas'
			String query = "INSERT INTO peliculas (titulo, genero, duracion, imagen) VALUES (?, ?, ?, ?)";
			PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			// Establecer los parámetros de la consulta de inserción
			statement.setString(1, pelicula.getTitulo());
			statement.setString(2, pelicula.getGenero());
			statement.setString(3, pelicula.getDuracion());
			statement.setString(4, pelicula.getImagen());
			statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()) {
				Long idPeli = rs.getLong(1);
				response.setContentType("application/json");
				String json = mapper.writeValueAsString(idPeli);
				response.getWriter().write(json);
			}
			response.setStatus(HttpServletResponse.SC_CREATED);
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
