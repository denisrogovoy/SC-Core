package at.uibk.dps.sc.core.capacity;

import org.opt4j.core.start.Constant;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

/**
 * Trivial implementation of a {@link CapacityCalculator}. Assumes that any
 * resource with a limited capacity can process a certain given number of tasks.
 * 
 * @author Fedor Smirnov
 */
@Singleton
public class CapacityCalculatorTaskNumber extends CapacityCalculatorAbstract {

  protected final int maximalTaskNumber;

  @Inject
  public CapacityCalculatorTaskNumber(@Constant(value = "maxTaskNumber",
      namespace = CapacityCalculatorTaskNumber.class) int maximalTaskNumber) {
    this.maximalTaskNumber = maximalTaskNumber;
  }

  @Override
  protected double getNonTrivialCapacityFraction(Task task, Resource res) {
    return 1.0 / maximalTaskNumber;
  }
}
