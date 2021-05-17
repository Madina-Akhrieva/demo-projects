class CategoryDataMapper {

  static toCategoryViewModel(serviceType, response) {
    return serviceType == TourService.graphQL ? this.#fromGraphQL(response) : this.#fromRest(response);
  }

  static #fromGraphQL(response) {
    const categories = response.data.data.tourCategories
    return categories.map(category => {
      return {
        id: category._metadata.id,
        displayName: category.displayName
      }
    });
  }

  static #fromRest(response) {
    const categories = response.data.results;
    return categories.map(category => {
      return {
        id: category['@id'],
        displayName: category.displayName
      }
    });
  }
}