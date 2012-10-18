package com.naitsoft.ihsan.server;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.prospectivesearch.ProspectiveSearchServiceFactory;


public class MatchResponseServlet extends HttpServlet {
	  private static final Logger log = Logger.getLogger(HelloWorldService.class.getName());
	/**                                                                                                                           
   * Handle Prospective Search match callbacks.                                                                                 
   */
  @Override
  public void doPost(final HttpServletRequest req, final HttpServletResponse rsp)
    throws ServletException, IOException {
    int resultsOffset = Integer.parseInt(req.getParameter("results_offset"));
    int resultsCount = Integer.parseInt(req.getParameter("results_count"));
    log.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxoffset: "+resultsOffset+" count:"+resultsCount);
    String [] reqSubIDs = req.getParameterValues("id");
    // Optional inclusion of matched entity if requested in original match(...) request:
    Entity matchedEntity = null;
//    if (req.hasParameter("document")) {
      matchedEntity =
        ProspectiveSearchServiceFactory.getProspectiveSearchService().getDocument(req);
//    }

    // Do something based on match...
  }
  @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
}