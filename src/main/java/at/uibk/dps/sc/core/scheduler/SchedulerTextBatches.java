package at.uibk.dps.sc.core.scheduler;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.opt4j.core.start.Constant;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import at.uibk.dps.ee.guice.starter.VertxProvider;
import at.uibk.dps.ee.model.graph.SpecificationProvider;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
import at.uibk.dps.ee.model.properties.PropertyServiceMapping;
import at.uibk.dps.ee.model.properties.PropertyServiceMapping.EnactmentMode;
import at.uibk.dps.sc.core.capacity.CapacityCalculator;
import net.sf.opendse.model.Mapping;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;


/**
 * Random scheduler which excludes the serverless options in case they exceed a
 * defined payload-size threshold.
 * 
 * @author Fedor Smirnov
 *
 */
public class SchedulerTextBatches extends SchedulerRandom {

  protected final int batchNumberLimit;
  protected int batchSizeInput;

  /**
   * Injection constructor
   * 
   * @param specProvider see parent
   * @param random see parent
   * @param mappingsToPick see parent
   * @param sizeOfBatch the data size threshold for local execution
   */
  @Inject
  public SchedulerTextBatches(final SpecificationProvider specProvider, final Random random,
      @Constant(namespace = SchedulerRandom.class,
          value = "mappingsToPick") final int mappingsToPick,
      @Constant(namespace = SchedulerTextBatches.class,
          value = "batchNumberLimit") final int sizeOfBatch,
      final CapacityCalculator capCalc, final VertxProvider vProv) {
    super(specProvider, random, mappingsToPick, capCalc, vProv);
    this.batchNumberLimit = sizeOfBatch;
  }

  @Override
  protected Set<Mapping<Task, Resource>> chooseMappingSubset(final Task task,
      final Set<Mapping<Task, Resource>> mappingOptions) {
    return super.chooseMappingSubset(task, mappingOptions.stream()
        .filter(mapping -> excludeMapping(mapping, task)).collect(Collectors.toSet()));
  }

  /**
   * Returns true iff the task in the given mapping has a data input smaller than
   * the defined threshold.
   * 
   * @param mapping the given mapping
   * @return true iff the task in the given mapping has a data input smaller than
   *         the defined threshold
   */
  protected boolean excludeMapping(final Mapping<Task, Resource> mapping, final Task process) {
    final JsonObject input = PropertyServiceFunction.getInput(process);

    final JsonElement numberOfWords = input.get("batchSize");
    final JsonElement originalStr = input.get("originalStr");
    if (numberOfWords != null && originalStr != null) {
        final String text = originalStr.getAsString();
        final String[] splited = text.split("\\s+");
        batchSizeInput = splited.length / numberOfWords.getAsInt();
    }
    final boolean overThreshold = batchSizeInput >= batchNumberLimit;
    final boolean overAndServerless = overThreshold
        && PropertyServiceMapping.getEnactmentMode(mapping).equals(EnactmentMode.Serverless);
    final boolean underAndLocal = !overThreshold
        && PropertyServiceMapping.getEnactmentMode(mapping).equals(EnactmentMode.Local);
    return overAndServerless || underAndLocal;
  }
}
