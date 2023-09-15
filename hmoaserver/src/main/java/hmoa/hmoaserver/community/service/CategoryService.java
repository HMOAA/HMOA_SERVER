package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
}
