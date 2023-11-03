package polyu.learn.bbssoadfs.operation;

public class OperationHandlerContext {
	private String operation;
	
	public OperationHandlerContext(String operation){
		this.operation = operation;
	}
	
	public void run() throws Exception{
		OperationHandler operationHandler = OperationHandlerFactory.getOperationHandler(operation);
		operationHandler.run();
	}
	
	public void check() throws Exception{
		OperationHandler operationHandler = OperationHandlerFactory.getOperationHandler(operation);
		operationHandler.check();
	}
	
	public void update() throws Exception{
		OperationHandler operationHandler = OperationHandlerFactory.getOperationHandler(operation);
		operationHandler.update();
	}
}
