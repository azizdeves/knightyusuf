package com.naitsoft.ihsan.shared;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName(value = "com.naitsoft.ihsan.server.IhsanService", locator = "com.naitsoft.ihsan.server.IhsanServiceLocator")
public interface IhsanRequest extends RequestContext {

	Request<VolunteerDetailProxy> createVolunteerDetail();

	Request<VolunteerDetailProxy> readVolunteerDetail(Long id);

	Request<VolunteerDetailProxy> updateVolunteerDetail(
			VolunteerDetailProxy volunteerdetail);

	Request<Void> deleteVolunteerDetail(VolunteerDetailProxy volunteerdetail);

	Request<List<VolunteerDetailProxy>> queryVolunteerDetails();

	Request<StreamItemProxy> createStreamItem();

	Request<StreamItemProxy> readStreamItem(Long id);

	Request<StreamItemProxy> updateStreamItem(StreamItemProxy streamitem);

	Request<Void> deleteStreamItem(StreamItemProxy streamitem);

	Request<List<StreamItemProxy>> queryStreamItems();

	Request<VolunteerProxy> createVolunteer();

	Request<VolunteerProxy> readVolunteer(Long id);

	Request<VolunteerProxy> updateVolunteer(VolunteerProxy volunteer);

	Request<Void> deleteVolunteer(VolunteerProxy volunteer);

	Request<List<VolunteerProxy>> queryVolunteers();

	Request<ProjectProxy> createProject();

	Request<ProjectProxy> readProject(Long id);

	Request<ProjectProxy> updateProject(ProjectProxy project);

	Request<Void> deleteProject(ProjectProxy project);

	Request<List<ProjectProxy>> queryProjects();

	Request<OrgDetailProxy> createOrgDetail();

	Request<OrgDetailProxy> readOrgDetail(Long id);

	Request<OrgDetailProxy> updateOrgDetail(OrgDetailProxy orgdetail);

	Request<Void> deleteOrgDetail(OrgDetailProxy orgdetail);

	Request<List<OrgDetailProxy>> queryOrgDetails();

	Request<ProjectDetailProxy> createProjectDetail();

	Request<ProjectDetailProxy> readProjectDetail(Long id);

	Request<ProjectDetailProxy> updateProjectDetail(
			ProjectDetailProxy projectdetail);

	Request<Void> deleteProjectDetail(ProjectDetailProxy projectdetail);

	Request<List<ProjectDetailProxy>> queryProjectDetails();

	Request<OrganizationProxy> createOrganization();

	Request<OrganizationProxy> readOrganization(Long id);

	Request<OrganizationProxy> updateOrganization(OrganizationProxy organization);

	Request<Void> deleteOrganization(OrganizationProxy organization);

	Request<List<OrganizationProxy>> queryOrganizations();

}
