package polyu.learn.bbssoadfs.operation.sis;

import polyu.learn.bbssoadfs.operation.Callbackable;

public abstract class SISClient {
	private SISClientContext sisClientContext;
	private Callbackable sisClientCallback;
	public SISClient(SISClientContext sisClientContext, Callbackable sisClientCallback){
		this.sisClientContext = sisClientContext;
		this.sisClientCallback = sisClientCallback;
	}
	
	public SISClientContext getSISClientContext() {
		return sisClientContext;
	}

	public void setSISClientContext(SISClientContext sisClientContext) {
		this.sisClientContext = sisClientContext;
	}
	

	public Callbackable getSISClientCallback() {
		return sisClientCallback;
	}

	public void setSISClientCallback(Callbackable sisClientCallback) {
		this.sisClientCallback = sisClientCallback;
	}

	public abstract void query() throws SISException;
}
