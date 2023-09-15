package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;

import java.util.List;

public interface CommunityService {
    List<Community> getAllCommunitys();
    Community getCommunityById(Long id);
}
