package io.vlingo.xoom.petclinic.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.common.Completes;

import io.vlingo.xoom.petclinic.infrastructure.AnimalTypeData;

public interface AnimalTypeQueries {
  Completes<AnimalTypeData> animalTypeOf(String id);
  Completes<Collection<AnimalTypeData>> animalTypes();
}