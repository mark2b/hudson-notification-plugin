package com.tikal.hudson.plugins.notification;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import hudson.model.Result;

public class PhaseTest {

	@Test
	public void testIsRun() {
		try {
			Endpoint endPoint = new Endpoint(null);
			Method isRunMethod = Phase.class.getDeclaredMethod("isRun", Endpoint.class, Result.class, Result.class);
			isRunMethod.setAccessible(true);

			assertEquals("returns true for null endpoint event",
					isRunMethod.invoke(Phase.QUEUED, endPoint, null, null), Boolean.TRUE);
			
			endPoint.setEvent("all");
			for (Phase phaseValue : Phase.values()) {
				assertEquals("all Event returns true for Phase " + phaseValue.toString(),
						isRunMethod.invoke(phaseValue, endPoint, null, null), Boolean.TRUE);
			}

			endPoint.setEvent("queued");
			assertEquals("queued Event returns true for Phase Queued",
					isRunMethod.invoke(Phase.QUEUED, endPoint, null, null), Boolean.TRUE);
			assertEquals("queued Event returns false for Phase Started",
					isRunMethod.invoke(Phase.STARTED, endPoint, null, null), Boolean.FALSE);
			
			endPoint.setEvent("started");
			assertEquals("started Event returns true for Phase Started",
					isRunMethod.invoke(Phase.STARTED, endPoint, null, null), Boolean.TRUE);
			assertEquals("started Event returns false for Phase Completed",
					isRunMethod.invoke(Phase.COMPLETED, endPoint, null, null), Boolean.FALSE);

			
			endPoint.setEvent("completed");
			assertEquals("completed Event returns true for Phase Completed",
					isRunMethod.invoke(Phase.COMPLETED, endPoint, null, null), Boolean.TRUE);
			assertEquals("completed Event returns false for Phase Finalized",
					isRunMethod.invoke(Phase.FINALIZED, endPoint, null, null), Boolean.FALSE);

			
			endPoint.setEvent("finalized");
			assertEquals("finalized Event returns true for Phase Finalized",
					isRunMethod.invoke(Phase.FINALIZED, endPoint, null, null), Boolean.TRUE);
			assertEquals("finalized Event returns true for Phase Queued",
					isRunMethod.invoke(Phase.QUEUED, endPoint, null, null), Boolean.FALSE);

			
			endPoint.setEvent("failed");
			assertEquals("failed Event returns false for Phase Finalized and no status",
					isRunMethod.invoke(Phase.FINALIZED, endPoint, null, null), Boolean.FALSE);
			assertEquals("failed Event returns false for Phase Finalized and success status",
					isRunMethod.invoke(Phase.FINALIZED, endPoint, Result.SUCCESS, null), Boolean.FALSE);
			assertEquals("failed Event returns true for Phase Finalized and success failure",
					isRunMethod.invoke(Phase.FINALIZED, endPoint, Result.FAILURE, null), Boolean.TRUE);
			assertEquals("failed Event returns false for Phase not Finalized and success failure",
					isRunMethod.invoke(Phase.COMPLETED, endPoint, Result.FAILURE, null), Boolean.FALSE);

			endPoint.setEvent("failedAndFirstSuccess");
			assertEquals("failedAndFirstSuccess Event returns false for Phase Finalized and no status",
					isRunMethod.invoke(Phase.FINALIZED, endPoint, null, null), Boolean.FALSE);
			assertEquals("failedAndFirstSuccess Event returns false for Phase Finalized and no previous status",
					isRunMethod.invoke(Phase.FINALIZED, endPoint, Result.SUCCESS, null), Boolean.FALSE);
			assertEquals("failedAndFirstSuccess Event returns true for Phase Finalized and no previous status and failed status",
					isRunMethod.invoke(Phase.FINALIZED, endPoint, Result.FAILURE, null), Boolean.TRUE);
			assertEquals("failedAndFirstSuccess Event returns true for Phase Finalized and failed status",
					isRunMethod.invoke(Phase.FINALIZED, endPoint, Result.FAILURE, Result.FAILURE), Boolean.TRUE);
			assertEquals("failedAndFirstSuccess Event returns true for Phase Finalized and success status with previous status of failure",
					isRunMethod.invoke(Phase.FINALIZED, endPoint, Result.SUCCESS, Result.FAILURE), Boolean.TRUE);
			assertEquals("failedAndFirstSuccess Event returns false for Phase Finalized and success status with previous status of success",
					isRunMethod.invoke(Phase.FINALIZED, endPoint, Result.SUCCESS, Result.SUCCESS), Boolean.FALSE);
			assertEquals("failedAndFirstSuccess Event returns false for Phase not Finalized",
					isRunMethod.invoke(Phase.COMPLETED, endPoint, Result.SUCCESS, Result.FAILURE), Boolean.FALSE);

			
			

		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
