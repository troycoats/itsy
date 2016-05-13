package com.itsy.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.itsy.constants.Constants;

/**
 * Servlet implementation class for Servlet: TaskListServlet
 *
 */
@WebServlet("/TaskListServlet")
public class TaskListServlet extends BaseServlet {
	private static final long serialVersionUID = 283473783;
	
	private static Logger logger = Logger.getLogger(TaskListServlet.class);
	
	public TaskListServlet() {
		super();
	}
	
	/**
	 * handle servlet requests
	 * @param request HttpServletRequest 
	 * @param response HttpServletResponse
	 * @return void
	 */
	protected void performTask(HttpServletRequest request, HttpServletResponse response) {
		/*
		PageMode mode = PageMode.resolveEnumFromString(URLEncryption.getParamAsString(request, "mode"));
		switch (mode) {
 			case FIND:    find(request, response);   	break;			
			case DELETE:  delete(request, response); 	break;
		}
		*/		
		
		try {
			redirect(Constants.TASK_LIST_PAGE, Constants.TASK_LIST_PAGE_PERMISSIONS, request, response);
		} catch (Throwable theException) {
			logger.error("Exception retrieving task list information");		
		}
	}
	
	/**
	 * obtain the information from the request
	 * @param request HttpServletRequest 
	 * @return TaskListBO
	 */
	/*
	private TaskListBO getFromRequest(HttpServletRequest request) {
		TaskListBO taskListBO = new TaskListBO();
		try {
			if ("true".equalsIgnoreCase(URLEncryption.getParameter(request, "requested"))) {
				taskListBO.setUserId(URLEncryption.getParamAsInt(request, "userid"));
			} else {
				taskListBO.setUserId((Integer) request.getSession().getAttribute(VoiceConstants.USER_ID));
			}
		} catch (Exception e) {
			logger.error(".getFromRequest() taskList [Exception]", e);
		}
		return taskListBO;
	}
	*/
	
	/**
	 * add page objects needed for the UI
	 * @param taskListBO 
	 * @return TaskListBO
	 */
	/*
	private TaskListBO addPageObjects(HttpServletRequest request, TaskListBO taskListBO) {
		UserProfileBO currentUser = UserProfileBO.getCurrentLoggedInUser(request);
		
		// show filter
		boolean showFilter = RoleUtil.userHasRole(request, Role.IT_ADMIN, Role.GAL_ADMIN, Role.OFFICE_COORDINATOR, Role.ADVOCATE_COORDINATOR, Role.HELP_DESK, Role.STAFF, Role.GAL_ATTORNEY);
		taskListBO.setAttribute("ShowFilter", showFilter ? "Y" : "N");
		if (showFilter) {
			try {
				int[] officeIds = GalOfficeUtil.getOfficeIds(currentUser.getUserId());
				if (officeIds != null && officeIds.length > 0) {
					List<UserPO> users = null;
					users = UserGalOfficeXrefBO.findByOfficesAndRoles(officeIds, RoleUtil.getSubordinateRolesForCurrentUser(RoleArea.WORK_QUEUE.queue_ind, request));
					if (users != null && users.size() > 0) {
						taskListBO.setAttribute("UserOptions", OptionUtil.createSelectOptionList(taskListBO.getUserIdAsString(), users, null, false));
					}
					users = null;
				}
				officeIds = null;
			} catch (Exception e) {
				taskListBO.setAttribute("Error", "TaskListServlet addPageObjects error -- " + e.toString());
			}
		}
		
		taskListBO.setAttribute("ViewingOwnList", (currentUser.getUserId() == taskListBO.getUserId() ? "Y" : "N"));
		
		currentUser = null;
		
		return taskListBO;
	}
	*/
	
	/**
	 * handle find request
	 * @param request HttpServletRequest 
	 * @param response HttpServletResponse
	 * @return void
	 */
	/*
	private void find(HttpServletRequest request, HttpServletResponse response) {
		TaskListBO taskListBO = getFromRequest(request);
		
		List<TaskListBO> results = null;
		try {
		    try {
	    		results = TaskListBO.findAll(taskListBO.getUserId(), DataFetch.FULL);
		    	request.setAttribute("tooMany", "No tasks at this time.");
		    } catch (TooManyResultsException tme) {
		    	request.setAttribute("tooMany", XMLConfig.getProperty("PROPERTY.max_results_message"));
		    }
	    } catch (Exception e) {
			logger.error("Exception retrieving the results for the search criteria ", e);
			taskListBO.setAttribute("Error", e.toString());
		}
		request.setAttribute(Constants.RESULTS, results);
		request.setAttribute("searchCriteria", addPageObjects(request, taskListBO));
		
		try {
			redirect(TASK_LIST_PAGE, PERMISSIONS, request, response);
		} catch (Throwable theException) {
			logger.error("Exception retrieving task list information");		
		}
		
		taskListBO = null;
		results = null;
	}
	*/
	
	/**
	 * handle delete request
	 * @param request HttpServletRequest 
	 * @param response HttpServletResponse
	 * @return void
	 */
	/*
	private void delete(HttpServletRequest request, HttpServletResponse response) {
		TaskListBO taskListBO = getFromRequest(request);
		taskListBO.setTaskListId(URLEncryption.getParamAsInt(request, "tasklistid"));
		taskListBO.setTaskCategoryId(URLEncryption.getParamAsInt(request, "taskcategoryid"));
		taskListBO.setCaseProfileId(URLEncryption.getParamAsInt(request, "caseprofileid"));
		try {
			TaskListBO.delete(taskListBO);			
		} catch (Exception e) {
			logger.error("Exception deleting record ", e);
			String errorMessage = e.getMessage();
			if (errorMessage.contains("still being referenced")) {
				errorMessage = "Record cannot be deleted, it is associated with other records.";
			}					
		}
		taskListBO = null;
		
		find(request, response);
	}
	*/
}