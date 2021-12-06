  
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
   * ������
   * @author admin
   * 
   * @Aspect�� ����Spring��ǰ����һ��������
   *
   */
   @Aspect
   public class LogAspects {

	     //��ȡ�������������ʽ
		//1����������
		//2����������������
	    //@Pointcut("execution(* spring.annotation.aop.MathCalculator.*(..))")
	    @Pointcut("execution(* div*(..))")
	    public void pointCut(){};
		 
		//@Before��Ŀ�귽��֮ǰ���룻�������ʽ��ָ�����ĸ��������룩
		@Before("pointCut()")
		public void logStart(JoinPoint joinPoint){
			Object[] args = joinPoint.getArgs();
			System.out.println(""+joinPoint.getSignature().getName()+"��������ǰ������@Before:�����б��ǣ�{"+Arrays.asList(args)+"}");
		}
		
		@After("pointCut()")
		public void logEnd(JoinPoint joinPoint){
			Object[] args = joinPoint.getArgs();
			System.out.println(""+joinPoint.getSignature().getName()+"�������к󡣡���@After:�����б��ǣ�{"+Arrays.asList(args)+"}");
		}
		
		//JoinPointһ��Ҫ�����ڲ�����ĵ�һλ
		@AfterReturning(value="pointCut()",returning="result")
		public void logReturn(JoinPoint joinPoint,Object result){
			System.out.println(""+joinPoint.getSignature().getName()+"�����������ء�����@AfterReturning:���н����{"+result+"}");
		}
		
		@AfterThrowing(value="pointCut()",throwing="exception")
		public void logException(JoinPoint joinPoint,Exception exception){
			System.out.println(""+joinPoint.getSignature().getName()+"�����쳣�������쳣��Ϣ��{"+exception+"}");
		}
  
  }
