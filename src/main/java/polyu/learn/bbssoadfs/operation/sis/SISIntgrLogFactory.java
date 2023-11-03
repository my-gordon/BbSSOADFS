package polyu.learn.bbssoadfs.operation.sis;

public class SISIntgrLogFactory {
	private static final SISIntgrLog sisIntgrLog = new SISIntgrLog();
	private SISIntgrLogFactory() {}
	public static SISIntgrLog getInstance(){
		return sisIntgrLog;
	}

}
