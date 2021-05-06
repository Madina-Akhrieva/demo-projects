class TourDataMapper {

  #baseContext;

  constructor(baseContext) {
    this.#baseContext = baseContext;
  }

  mapToTourViews(serviceType, response) {
    return this.#mapTourData(serviceType, response);
  }

  #mapTourData(serviceType, response) {
    return serviceType === TourService.graphQL ? this.#fromGraphQL(response) : this.#fromRest(response);
  }

  #fromRest(response) {
    let tours = response.data.results;
    return tours.map(tour => RestDataMapper.convert(tour, this.#baseContext));
  }

  #fromGraphQL(response) {
    let tours = response.data.data.tours;
    return tours.map(tour => GraphQLDataMapper.convert(tour));
  }
}

class GraphQLDataMapper {
  static convert(tourResponse) {
    return {
      path: tourResponse._metadata.path,
      name: tourResponse.name,
      duration: tourResponse.duration,
      image: { renditions: this.#buildImage(tourResponse) },
      tourTypes: this.#buildTourType(tourResponse),
      destination: tourResponse.destination
    };
  }

  static #buildImage(tourResponse) {
    return tourResponse.image.renditions.reduce(function(map, obj) {
      map[obj.renditionName] = obj;
      return map;
    }, {});
  }

  static #buildTourType(tourResponse) {
    return tourResponse.tourTypes.map(tourType => {
      return {
        icon: { link: tourType.icon.link },
        displayName: tourType.displayName
      }
    })
  }
}

class RestDataMapper {
  static convert(tourResponse, baseContext) {
    return {
      path: tourResponse['@path'],
      name: tourResponse.name,
      duration: tourResponse.duration,
      image: tourResponse.image,
      tourTypes: this.#buildTourType(tourResponse, baseContext),
      destination: tourResponse.destination
    };
  }

  static #buildTourType(tourResponse, baseContext) {
    return tourResponse.tourTypes.map(tourType => {
      return {
        icon: { link: `${baseContext}/dam/${tourType.icon}` },
        displayName: tourType.displayName
      };
    })
  }
}