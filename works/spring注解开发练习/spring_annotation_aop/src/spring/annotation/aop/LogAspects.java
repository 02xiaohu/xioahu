  
  package spring.annotation.aop;

  import java.util.Arrays;
  import org.aspectj.lang.JoinPoint;
  import org.aspectj.lang.annotation.After;
  import org.aspectj.lang.annotation.AfterReturning;
  import org.aspectj.lang.annotation.AfterThrowing;
  import org.aspectj.lang.annotation.Aspect;
  import org.aspectj.lang.annotation.Before;
  import org.aspectj.lang.annotation.Pointcut;
  /**
   * 切面类
   * @author admin
   * 
   * @Aspect： 告诉Spring当前类是一个切面类
   *
   */
   @Aspect
   public class LogAspects {

	     //抽取公共的切入点表达式
		//1、本类引用
		//2、其他的切面引用
	    //@Pointcut("execution(* spring.annotation.aop.MathCalculator.*(..))")
	    @Pointcut("execution(* div*(..))")
	    public void pointCut(){};
		 
		//@Before在目标方法之前切入；切入点表达式（指定在哪个方法切入）
		@Before("pointCut()")
		public void logStart(JoinPoint joinPoint){
			Object[] args = joinPoint.getArgs();
			System.out.println(""+joinPoint.getSignature().getName()+"方法运行前。。。@Before:参数列表是：{"+Arrays.asList(args)+"}");
		}
		
		@After("pointCut()")
		public void logEnd(JoinPoint joinPoint){
			Object[] args = joinPoint.getArgs();
			System.out.println(""+joinPoint.getSignature().getName()+"方法运行后。。。@After:参数列表是：{"+Arrays.asList(args)+"}");
		}
		
		//JoinPoint一定要出现在参数表的第一位
		@AfterReturning(value="pointCut()",returning="result")
		public void logReturn(JoinPoint joinPoint,Object result){
			System.out.println(""+joinPoint.getSignature().getName()+"方法正常返回。。。@AfterReturning:运行结果：{"+result+"}");
		}
		
		@AfterThrowing(value="pointCut()",throwing="exception")
		public void logException(JoinPoint joinPoint,Exception exception){
			System.out.println(""+joinPoint.getSignature().getName()+"方法异常。。。异常信息：{"+exception+"}");
		}
  
  }
