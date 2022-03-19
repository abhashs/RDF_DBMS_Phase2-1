package tests;

import java.io.*; 

import global.*;
import bufmgr.*;
import diskmgr.*;
import heap.*;
import iterator.*;
import index.*;
import quadrupleheap.*;

class QuadrupleDriver extends TestDriver implements GlobalConst {

	public QuadrupleDriver() {
		super("quadrupletest");
	}

	public boolean runTests ()  {

		System.out.println ("\n" + "Running " + testName() + " tests...." + "\n");

		SystemDefs sysdef = new SystemDefs( dbpath, 300, NUMBUF, "Clock" );

		// Kill anything that might be hanging around
		String newdbpath;
		String newlogpath;
		String remove_logcmd;
		String remove_dbcmd;
		String remove_cmd = "/bin/rm -rf ";

		newdbpath = dbpath;
		newlogpath = logpath;

		remove_logcmd = remove_cmd + logpath;
		remove_dbcmd = remove_cmd + dbpath;

		// Commands here is very machine dependent.  We assume
		// user are on UNIX system here
		try {
			Runtime.getRuntime().exec(remove_logcmd);
			Runtime.getRuntime().exec(remove_dbcmd);
		} 
		catch (IOException e) {
			System.err.println (""+e);
		}

		remove_logcmd = remove_cmd + newlogpath;
		remove_dbcmd = remove_cmd + newdbpath;

		//This step seems redundant for me.  But it's in the original
		//C++ code.  So I am keeping it as of now, just in case I
		//I missed something
		try {
			Runtime.getRuntime().exec(remove_logcmd);
			Runtime.getRuntime().exec(remove_dbcmd);
		} 
		catch (IOException e) {
			System.err.println (""+e);
		}

		//Run the tests. Return type different from C++
		boolean _pass = runAllTests();

		//Clean up again
		try {
			Runtime.getRuntime().exec(remove_logcmd);
			Runtime.getRuntime().exec(remove_dbcmd);
		} 
		catch (IOException e) {
			System.err.println (""+e);
		}

		System.out.println ("\n" + "..." + testName() + " tests ");
		System.out.println (_pass==OK ? "completely successfully" : "failed");
		System.out.println (".\n\n");

		return _pass;
	}

	protected boolean test1()
	{
		System.out.println("------------------------ TEST 1 --------------------------");

		boolean status = OK;

		// create a triple of appropriate size
		Quadruple t = new Quadruple();
		int size = t.size();

		// Create unsorted data file "test1.in"
		QID             tid;
		QuadrupleHeapfile        f = null;
		try 
		{
			f = new QuadrupleHeapfile("test1.in");
		}
		catch (Exception e) {
			status = FAIL;
			e.printStackTrace();
		}

		t = new Quadruple();
		try {
			t.setConfidence((float)1.5);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			tid = f.insertQuadruple(t.returnTupleByteArray());
			System.out.println(f.getQuadruple(tid).toString());
			f.getQuadruple(tid).print();
		}
		catch (Exception e) {
			status = FAIL;
			e.printStackTrace();
		}

		System.err.println("------------------- TEST 1 completed ---------------------\n");

		return status;
	}

	private byte[] toByteArray(LID subject, PID pred, LID object, float cf){
		byte[] out = new byte[28];
		try {
			Convert.setIntValue(subject.pageNo.pid , 0, out);
			Convert.setIntValue(subject.slotNo, 4, out);
			Convert.setIntValue(pred.pageNo.pid, 8, out);
			Convert.setIntValue(pred.slotNo, 12, out);
			Convert.setIntValue(object.pageNo.pid, 16, out);
			Convert.setIntValue(object.slotNo, 20, out);
			Convert.setFloValue(cf, 24, out);
		} catch (Exception e){
			e.printStackTrace();
		}
		return out;
	}

	protected boolean test2(){
		boolean status = true;

		System.out.println("------------------------ TEST 2 --------------------------");

		String[] names = {"abhash", "austin", "park", "adam", "ryan", "zuwei"};
		String[] preds = {"owns", "drives", "eats", "lives", "runs", "flies"};
		String[] objects = {"car", "food", "fordf150", "casino", "house", "boeing"};
		float[] cfs = {(float) .11, (float) .214, (float) .42, (float) .99, (float) .83, (float) .45};

		LID[] lids = new LID[12];
		PID[] pids = new PID[6];

		for(int i = 0; i < names.length; i++) {
			// System.out.println("before inserting name");
			lids[i] = SystemDefs.JavabaseDB.insertEntity(names[i]).returnLID();
			pids[i] = SystemDefs.JavabaseDB.insertPredicate(preds[i]);
			lids[i+6] = SystemDefs.JavabaseDB.insertEntity(objects[i]).returnLID();
		}

		for (int i = 0; i < 6; i++){
			// Quadruple quad = new Quadruple(toByteArray(lids[i], pids[i], lids[i+6], cfs[i]), 0);
			SystemDefs.JavabaseDB.insertQuadruple(toByteArray(lids[i], pids[i], lids[i+6], cfs[i]));
		}

		System.out.println(
			SystemDefs.JavabaseDB.getQuadrupleCnt() + " " +
			SystemDefs.JavabaseDB.getEntityCnt() + " " +
			SystemDefs.JavabaseDB.getPredicateCnt() + " " +
			SystemDefs.JavabaseDB.getObjectCnt() + " ");

		// // Create unsorted data file "test1.in"
		// QID             tid;
		// QuadrupleHeapfile        f = null;
		// try 
		// {
		// 	f = new QuadrupleHeapfile("test1.in");
		// }
		// catch (Exception e) {
		// 	status = FAIL;
		// 	e.printStackTrace();
		// }

		// q1 = new Quadruple();
		// try {
		// 	q1.setConfidence((float)1.5);
		// } catch (IOException e1) {
		// 	// TODO Auto-generated catch block
		// 	e1.printStackTrace();
		// }

		// try {
		// 	tid = f.insertQuadruple(q1.returnTupleByteArray());
		// }
		// catch (Exception e) {
		// 	status = FAIL;
		// 	e.printStackTrace();
		// }



		System.err.println("------------------- TEST 2 completed ---------------------\n");
		return status;
	}

	protected String testName()
	{
		return "Quadruple";
	}
}

public class QuadrupleTest
{
	public static void main(String argv[])
	{
		boolean triplestatus;

		QuadrupleDriver tripleDriver = new QuadrupleDriver();

		triplestatus = tripleDriver.runTests();

		if (triplestatus != true) {
			System.out.println("Error ocurred during triple tests");
		}
		else {
			System.out.println("Triple tests completed successfully");
		}
	}
}