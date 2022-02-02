package at.uibk.dps.sc.core.arbitration;

import java.util.List;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

/**
 * Interface for all classes used to arbitrate resources between multiple tasks.
 * 
 * @author Fedor Smirnov
 *
 */
public interface ResourceArbiter {

  /**
   * Returns the task which is to be scheduled next on the given resource
   * 
   * @param candidates the list of candidate tasks (ordered following the order of
   *        arrival -- tasks waiting for a longer time are at closer to the start
   *        of the list)
   * @param res the resource the tasks are waiting for
   * @return the task to schedule at the resource next
   */
  Task chooseTask(List<Task> candidates, Resource res);
}
