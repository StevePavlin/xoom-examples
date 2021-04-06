package io.vlingo.xoom.petclinic.model.pet;

import io.vlingo.xoom.petclinic.model.*;
import io.vlingo.common.Completes;

import io.vlingo.lattice.model.sourcing.EventSourced;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#sourced">EventSourced</a>
 */
public final class PetEntity extends EventSourced implements Pet {
  private PetState state;

  public PetEntity(final String id) {
    super(id);
    this.state = PetState.identifiedBy(id);
  }

  static {
    EventSourced.registerConsumer(PetEntity.class, PetRegistered.class, PetEntity::applyPetRegistered);
    EventSourced.registerConsumer(PetEntity.class, PetNameChanged.class, PetEntity::applyPetNameChanged);
    EventSourced.registerConsumer(PetEntity.class, PetBirthRecorded.class, PetEntity::applyPetBirthRecorded);
    EventSourced.registerConsumer(PetEntity.class, PetDeathRecorded.class, PetEntity::applyPetDeathRecorded);
    EventSourced.registerConsumer(PetEntity.class, PetKindCorrected.class, PetEntity::applyPetKindCorrected);
    EventSourced.registerConsumer(PetEntity.class, PetOwnerChanged.class, PetEntity::applyPetOwnerChanged);
  }

  @Override
  public Completes<PetState> register(final Name name, final Date birth, final Date death, final Kind kind, final Owner owner) {
    final PetState stateArg = state.register(name, birth, death, kind, owner);
    return apply(new PetRegistered(stateArg), () -> state);
  }

  @Override
  public Completes<PetState> changeName(final Name name) {
    final PetState stateArg = state.changeName(name);
    return apply(new PetNameChanged(stateArg), () -> state);
  }

  @Override
  public Completes<PetState> recordBirth(final Date birth) {
    final PetState stateArg = state.recordBirth(birth);
    return apply(new PetBirthRecorded(stateArg), () -> state);
  }

  @Override
  public Completes<PetState> recordDeath(final Date death) {
    final PetState stateArg = state.recordDeath(death);
    return apply(new PetDeathRecorded(stateArg), () -> state);
  }

  @Override
  public Completes<PetState> correctKind(final Kind kind) {
    final PetState stateArg = state.correctKind(kind);
    return apply(new PetKindCorrected(stateArg), () -> state);
  }

  @Override
  public Completes<PetState> changeOwner(final Owner owner) {
    final PetState stateArg = state.changeOwner(owner);
    return apply(new PetOwnerChanged(stateArg), () -> state);
  }

  private void applyPetRegistered(final PetRegistered event) {
    //TODO: Event is missing death; using null instead
    state = state.register(event.name, event.birth, null, event.kind, event.owner);
  }

  private void applyPetNameChanged(final PetNameChanged event) {
    state = state.changeName(event.name);
  }

  private void applyPetBirthRecorded(final PetBirthRecorded event) {
    state = state.recordBirth(event.birth);
  }

  private void applyPetDeathRecorded(final PetDeathRecorded event) {
    state = state.recordDeath(event.death);
  }

  private void applyPetKindCorrected(final PetKindCorrected event) {
    state = state.correctKind(event.kind);
  }

  private void applyPetOwnerChanged(final PetOwnerChanged event) {
    state = state.changeOwner(event.owner);
  }

  /*
   * Restores my initial state by means of {@code state}.
   *
   * @param snapshot the {@code PetState} holding my state
   * @param currentVersion the int value of my current version; may be helpful in determining if snapshot is needed
   */
  @Override
  protected <PetState> void restoreSnapshot(final PetState snapshot, final int currentVersion) {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/vlingo-lattice/entity-cqrs#eventsourced
  }

  /*
   * Answer the valid {@code PetState} instance if a snapshot should
   * be taken and persisted along with applied {@code Source<T>} instance(s).
   *
   * @return PetState
   */
  @Override
  protected PetState snapshot() {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/vlingo-lattice/entity-cqrs#eventsourced
    return null;
  }
}
