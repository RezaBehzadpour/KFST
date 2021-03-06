/*
 * Kurdistan Feature Selection Tool (KFST) is an open-source tool, developed
 * completely in Java, for performing feature selection process in different
 * areas of research.
 * For more information about KFST, please visit:
 *     http://kfst.uok.ac.ir/index.html
 *
 * Copyright (C) 2016-2018 KFST development team at University of Kurdistan,
 * Sanandaj, Iran.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package KFST.featureSelection.wrapper.PSOBasedMethods.BPSO;

import KFST.featureSelection.wrapper.PSOBasedMethods.BasicPSO;
import KFST.util.ArraysFunc;
import java.util.ArrayList;

/**
 * This java class is used to implement feature selection method based on binary
 * particle swarm optimization (BPSO) in which the type of Swarm is extended
 * from Swarm class.
 *
 * @author Sina Tabakhi
 * @see KFST.featureSelection.wrapper.PSOBasedMethods.BasicPSO
 * @see KFST.featureSelection.wrapper.WrapperApproach
 * @see KFST.featureSelection.FeatureSelection
 */
public class BPSO extends BasicPSO<Swarm> {

    /**
     * initializes the parameters
     *
     * @param arguments array of parameters contains ( <code>path</code>,
     * <code>numFeatures</code>, <code>classifierType</code>,
     * <code>selectedClassifierPan</code>, <code>numIteration</code>
     * <code>populationSize</code>, <code>inertiaWeight</code>,
     * <code>parameter c1</code>, <code>parameter c2</code>,
     * <code>startPosInterval</code>, <code>endPosInterval</code>,
     * <code>minVelocity</code>, <code>maxVelocity</code>, <code>kFolds</code>)
     * in which <code><b><i>path</i></b></code> is the path of the project,
     * <code><b><i>numFeatures</i></b></code> is the number of original features
     * in the dataset, <code><b><i>classifierType</i></b></code> is the
     * classifier type for evaluating the fitness of a solution,
     * <code><b><i>selectedClassifierPan</i></b></code> is the selected
     * classifier panel, <code><b><i>numIteration</i></b></code> is the maximum
     * number of allowed iterations that algorithm repeated,
     * <code><b><i>populationSize</i></b></code> is the size of population of
     * candidate solutions, <code><b><i>inertiaWeight</i></b></code> is the
     * inertia weight in the velocity updating rule, <code><b><i>parameter
     * c1</i></b></code> is the acceleration constant in the velocity updating
     * rule, <code><b><i>parameter c2</i></b></code> is the acceleration
     * constant in the velocity updating rule,
     * <code><b><i>startPosInterval</i></b></code> is the position interval
     * start value, <code><b><i>endPosInterval</i></b></code> is the position
     * interval end value, <code><b><i>minVelocity</i></b></code> is the
     * velocity interval start value, <code><b><i>maxVelocity</i></b></code> is
     * the velocity interval end value, and <code><b><i>kFolds</i></b></code> is
     * the number of equal sized subsamples that is used in k-fold cross
     * validation
     */
    public BPSO(Object... arguments) {
        super(arguments);
        swarm = new Swarm();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected int[] createSelectedFeatureSubset() {
        ArrayList<Integer> featSubset = new ArrayList();
        for (int i = 0; i < Swarm.PROBLEM_DIMENSION; i++) {
            if (swarm.getGBest()[i]) {
                featSubset.add(i);
            }
        }
        return featSubset.stream().mapToInt(i -> i).toArray();
    }

    /**
     * starts the feature selection process by binary particle swarm
     * optimization (BPSO) method
     */
    @Override
    public void evaluateFeatures() {
        Swarm.fitnessEvaluator.createTempDirectory();
        Swarm.fitnessEvaluator.setDataInfo(trainSet, nameFeatures, classLabel);
        swarm.initialization();
        for (int i = 0; i < NUM_ITERATION; i++) {
            System.out.println("\nIteration " + i + ":\n\n");
            swarm.evaluateFitness();
            swarm.updatePersonalBest();
            swarm.updateGlobalBest();
            swarm.updateParticleVelocity();
            swarm.updateParticlePosition();
        }

        selectedFeatureSubset = createSelectedFeatureSubset();
        numSelectedFeature = selectedFeatureSubset.length;

        ArraysFunc.sortArray1D(selectedFeatureSubset, false);

//        for (int i = 0; i < numSelectedFeature; i++) {
//            System.out.println("ranked  = " + selectedFeatureSubset[i]);
//        }

        Swarm.fitnessEvaluator.deleteTempDirectory();
    }
}
