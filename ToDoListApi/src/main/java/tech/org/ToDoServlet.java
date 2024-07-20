package tech.org;

import com.fasterxml.jackson.databind.ObjectMapper;
import tech.model.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@WebServlet("/totask")
public class ToDoServlet extends HttpServlet {

	private List<Task> tasks = new ArrayList<>();
	private ObjectMapper objectMapper = new ObjectMapper();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();

		if (pathInfo == null || pathInfo.equals("/")) {
			// Return all tasks
			response.setContentType("application/json");
			response.getWriter().write(objectMapper.writeValueAsString(tasks));
		} else {
			// Return specific task by id
			String[] parts = pathInfo.split("/");
			String taskId = parts[1];
			Task task = findTaskById(taskId);
			if (task != null) {
				response.setContentType("application/json");
				response.getWriter().write(objectMapper.writeValueAsString(task));
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Task newTask = objectMapper.readValue(request.getReader(), Task.class);
		tasks.add(newTask);
		response.setStatus(HttpServletResponse.SC_CREATED);
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] parts = request.getPathInfo().split("/");
		String taskId = parts[1];
		Task updatedTask = objectMapper.readValue(request.getReader(), Task.class);

		Task existingTask = findTaskById(taskId);
		if (existingTask != null) {
			existingTask.setTitle(updatedTask.getTitle());
			existingTask.setDescription(updatedTask.getDescription());
			existingTask.setCompleted(updatedTask.isCompleted());
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	 protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        String[] parts = request.getPathInfo().split("/");
	        String taskId = parts[1];

	        Task task = findTaskById(taskId);
	        if (task != null) {
	            tasks.remove(task);
	            response.setStatus(HttpServletResponse.SC_OK);
	        } else {
	        	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	        }
	    }
	 
	 private Task findTaskById(String tid) {
		 int id=Integer.parseInt(tid);
	        for (Task task : tasks) {
	            if (task.getId()==id) {
	                return task;
	            }
	        }
	        return null;
	    }


}
