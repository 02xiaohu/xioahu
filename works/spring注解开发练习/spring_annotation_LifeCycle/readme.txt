  һ. bean���������ڣ�
		bean����---��ʼ��----���ٵĹ���
 * ��������bean���������ڣ�
 * ���ǿ����Զ����ʼ�������ٷ�����������bean���е���ǰ�������ڵ�ʱ�������������Զ���ĳ�ʼ�������ٷ���
 * 
 * ���죨���󴴽���
 * 		��ʵ����������������ʱ�򴴽�����
 * 		��ʵ������ÿ�λ�ȡ��ʱ�򴴽����� 
 * 
 * BeanPostProcessor.postProcessBeforeInitialization
 * ��ʼ����
 * 		���󴴽���ɣ�����ֵ�ã����ó�ʼ������������
 * BeanPostProcessor.postProcessAfterInitialization
 * ���٣�
 * 		��ʵ���������رյ�ʱ��
 * 		��ʵ������������������bean����������������ٷ�����
 * 
 * 
 * �����õ����������е�BeanPostProcessor������ִ��beforeInitialization��
 * һ������null������forѭ��������ִ�к����BeanPostProcessor.postProcessorsBeforeInitialization
 * 
 * BeanPostProcessorԭ��
 * populateBean(beanName, mbd, instanceWrapper);��bean�������Ը�ֵ
 * initializeBean
 * {
 * applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
 * invokeInitMethods(beanName, wrappedBean, mbd);ִ���Զ����ʼ��
 * applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
 *}
 * 
 * 
 * 
 * 1����ָ����ʼ�������ٷ�����
 * 		ͨ��@Beanָ��init-method��destroy-method��
 * 2����ͨ����Beanʵ��InitializingBean�������ʼ���߼�����
 * 				DisposableBean�����������߼���;
 * 3��������ʹ��JSR250��
 * 		@PostConstruct����bean������ɲ������Ը�ֵ��ɣ���ִ�г�ʼ������
 * 		@PreDestroy������������bean֮ǰ֪ͨ���ǽ���������
 * 4����BeanPostProcessor��interface����bean�ĺ��ô�������
 * 		��bean��ʼ��ǰ�����һЩ��������
 * 		postProcessBeforeInitialization:�ڳ�ʼ��֮ǰ����
 * 		postProcessAfterInitialization:�ڳ�ʼ��֮����
 * 
 * Spring�ײ�� BeanPostProcessor ��ʹ�ã�
 * 		bean��ֵ��ע�����������@Autowired����������ע�⹦�ܣ�@Async,xxx BeanPostProcessor;
 
 ��.ʹ��@PropertySource��ȡ�ⲿ�����ļ��е�k/v���浽���еĻ���������;�������ⲿ�������ļ��Ժ�ʹ��${}ȡ�������ļ���ֵ
        @PropertySource("classpath:/resources/person.properties")
        @Value("${person.nickName}")
		private String nickName;
		
��.�Զ��������Ҫʹ��Spring�����ײ��һЩ�����ApplicationContext��BeanName��EmbeddedValueResolver����
 * 		�Զ������ʵ��xxxAware���ڴ��������ʱ��ͨ�����ô����� ���ýӿڹ涨�ķ���ע����������Aware��
 * 		��Spring�ײ�һЩ���ע�뵽�Զ����Bean�У�
 * 		xxxAware������ʹ��xxxProcessor��
 * 			ApplicationContextAware==��ApplicationContextAwareProcessor��
 