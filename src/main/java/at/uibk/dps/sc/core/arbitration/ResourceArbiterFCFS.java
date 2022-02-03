package at.uibk.dps.sc.core.arbitration;

import java.util.ArrayList;
import java.util.List;
import com.google.inject.Singleton;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

/**
 * A {@link ResourceArbiter} implementing the FCFS arbitration scheme.
 * 
 * @author Fedor Smirnov
 *
 */
@Singleton
public class ResourceArbiterFCFS implements ResourceArbiter {

  @Override
  public List<Task> prioritizeTasks(List<Task> candidates, Resource res) {
    // return a copy of the input, since it is already ordered following the arrival
    // time
    return new ArrayList<>(candidates);
  }
}
