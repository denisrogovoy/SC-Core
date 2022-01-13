package at.uibk.dps.sc.core.scheduler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import at.uibk.dps.ee.model.graph.EnactmentGraph;
import at.uibk.dps.ee.model.graph.EnactmentSpecification;
import at.uibk.dps.ee.model.graph.MappingsConcurrent;
import at.uibk.dps.ee.model.graph.ResourceGraph;
import at.uibk.dps.ee.model.graph.SpecificationProvider;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUser;
import at.uibk.dps.sc.core.capacity.CapacityCalculatorNone;
import net.sf.opendse.model.Mapping;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

public class SchedulerSingleOptionTest {

  @Test
  public void test() {
    Task task = PropertyServiceFunctionUser.createUserTask("bla", "addition");
    Resource res = new Resource("res");
    Mapping<Task, Resource> mapping = new Mapping<Task, Resource>("m", task, res);
    Set<Mapping<Task, Resource>> expected = new HashSet<>();
    expected.add(mapping);
    MappingsConcurrent mappings = new MappingsConcurrent();
    mappings.addMapping(mapping);
    EnactmentGraph eGraph = new EnactmentGraph();
    ResourceGraph rGraph = new ResourceGraph();
    EnactmentSpecification spec = new EnactmentSpecification(eGraph, rGraph, mappings, "");
    SpecificationProvider providerMock = mock(SpecificationProvider.class);
    when(providerMock.getMappings()).thenReturn(mappings);
    when(providerMock.getSpecification()).thenReturn(spec);
    SchedulerSingleOption tested =
        new SchedulerSingleOption(providerMock, new CapacityCalculatorNone());
    assertEquals(expected, tested.scheduleTask(task));
  }

  @Test
  public void testMoreThanOne() {
    assertThrows(IllegalArgumentException.class, () -> {
      Task task = PropertyServiceFunctionUser.createUserTask("bla", "addition");
      Resource res = new Resource("res");
      Resource res2 = new Resource("res2");
      Mapping<Task, Resource> mapping = new Mapping<Task, Resource>("m", task, res);
      Mapping<Task, Resource> mapping2 = new Mapping<Task, Resource>("m2", task, res2);
      MappingsConcurrent mappings = new MappingsConcurrent();
      mappings.addMapping(mapping);
      mappings.addMapping(mapping2);
      EnactmentGraph eGraph = new EnactmentGraph();
      ResourceGraph rGraph = new ResourceGraph();
      EnactmentSpecification spec = new EnactmentSpecification(eGraph, rGraph, mappings, "");
      SpecificationProvider providerMock = mock(SpecificationProvider.class);
      when(providerMock.getMappings()).thenReturn(mappings);
      when(providerMock.getSpecification()).thenReturn(spec);
      SchedulerSingleOption tested =
          new SchedulerSingleOption(providerMock, new CapacityCalculatorNone());
      tested.scheduleTask(task);
    });
  }
}
