package at.uibk.dps.sc.core.modules;

import org.opt4j.core.config.annotations.Info;
import org.opt4j.core.config.annotations.Order;
import org.opt4j.core.config.annotations.Required;
import org.opt4j.core.start.Constant;
import at.uibk.dps.ee.guice.modules.EeModule;
import at.uibk.dps.sc.core.arbitration.ResourceArbiter;
import at.uibk.dps.sc.core.arbitration.ResourceArbiterFCFS;
import at.uibk.dps.sc.core.interpreter.ScheduleInterpreterUser;
import at.uibk.dps.sc.core.interpreter.ScheduleInterpreterUserSingle;
import at.uibk.dps.sc.core.scheduler.Scheduler;
import at.uibk.dps.sc.core.scheduler.SchedulerDataSize;
import at.uibk.dps.sc.core.scheduler.SchedulerLocalRes;
import at.uibk.dps.sc.core.scheduler.SchedulerRandom;
import at.uibk.dps.sc.core.scheduler.SchedulerSingleOption;
import at.uibk.dps.sc.core.scheduler.SchedulerTextBatches;

/**
 * The {@link SchedulerModule} configures the binding of the scheduling-related
 * interfaces.
 * 
 * @author Fedor Smirnov
 *
 */
public class SchedulerModule extends EeModule {

  /**
   * Enum defining different scheduling modes.
   * 
   * @author Fedor Smirnov
   */
  public enum SchedulingMode {
    /**
     * Expects a single scheduling option
     */
    SingleOption,
    /**
     * Random scheduling
     */
    Random,
    /**
     * Size threshold (should actually be a transmission option)
     */
    SizeConstraint,
    /**
     * Preferably uses resources with capacity limitations
     */
    LocalResources,

    BatchSize
  }

  /**
   * Enum defining the different resource arbitration schemes.
   * 
   * @author Fedor Smirnov
   *
   */
  public enum ResourceArbitration {
    /**
     * First come first serve (best-effort)
     */
    FCFS
  }

  @Order(1)
  @Info("The mode used to schedule user tasks.")
  public SchedulingMode schedulingMode = SchedulingMode.SingleOption;

  @Order(2)
  @Info("The number of mappings to pick for each user task.")
  @Constant(namespace = SchedulerRandom.class, value = "mappingsToPick")
  @Required(property = "schedulingMode", elements = {"Random", "SizeConstraint"})
  public int mappingsToPick = 1;

  @Order(3)
  @Info("Threshold in KB. Anything with an input with a larger size will be processed locally")
  @Constant(namespace = SchedulerDataSize.class, value = "sizeThreshold")
  @Required(property = "schedulingMode", elements = "SizeConstraint")
  public int sizeThresholdKb = 10;

  @Order(4)
  @Info("The mode used to arbitrate shared resources between tasks.")
  public ResourceArbitration resourceArbitration = ResourceArbitration.FCFS;

  @Order(5)
  @Info("Batch size")
  @Constant(namespace = SchedulerDataSize.class, value = "batchSize")
  @Required(property = "schedulingMode", elements = "BatchSize")
  public int batchSize = 10;

  @Override
  protected void config() {
    bind(ScheduleInterpreterUser.class).to(ScheduleInterpreterUserSingle.class);
    if (schedulingMode.equals(SchedulingMode.SingleOption)) {
      bind(Scheduler.class).to(SchedulerSingleOption.class);
    } else if (schedulingMode.equals(SchedulingMode.Random)) {
      bind(Scheduler.class).to(SchedulerRandom.class);
    } else if (schedulingMode.equals(SchedulingMode.SizeConstraint)) {
      bind(Scheduler.class).to(SchedulerDataSize.class);
    } else if (schedulingMode.equals(SchedulingMode.LocalResources)) {
      bind(Scheduler.class).to(SchedulerLocalRes.class);
    } else if (schedulingMode.equals(SchedulingMode.BatchSize)) {
      bind(Scheduler.class).to(SchedulerTextBatches.class);
    }
    if (resourceArbitration.equals(ResourceArbitration.FCFS)) {
      bind(ResourceArbiter.class).to(ResourceArbiterFCFS.class);
    }
  }

  public ResourceArbitration getResourceArbitration() {
    return resourceArbitration;
  }

  public void setResourceArbitration(final ResourceArbitration resourceArbitration) {
    this.resourceArbitration = resourceArbitration;
  }

  public SchedulingMode getSchedulingMode() {
    return schedulingMode;
  }

  public void setSchedulingMode(final SchedulingMode schedulingMode) {
    this.schedulingMode = schedulingMode;
  }

  public int getMappingsToPick() {
    return mappingsToPick;
  }

  public void setMappingsToPick(final int mappingsToPick) {
    this.mappingsToPick = mappingsToPick;
  }

  public int getSizeThresholdKb() {
    return sizeThresholdKb;
  }

  public void setSizeThresholdKb(final int sizeThresholdKb) {
    this.sizeThresholdKb = sizeThresholdKb;
  }

  public int getbatchSize() {
    return batchSize;
  }

  public void setbatchSize(final int batchSize) {
    this.batchSize = batchSize;
  }
}
