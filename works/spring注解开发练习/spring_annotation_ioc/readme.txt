ע�ⷽʽIOCע��
1.@Import 
@Import[���ٸ������е���һ�����]
	 	1����@Import(Ҫ���뵽�����е����)�������оͻ��Զ�ע����������idĬ����ȫ����
	  	2����ImportSelector:������Ҫ����������ȫ�������飻
	  	3����ImportBeanDefinitionRegistrar:�ֶ�ע��bean��������
@Import(value=Color.class)//�����л��Զ�ע����������idĬ����ȫ����
@Import({Color.class,Red.class,MyImportSelector.class,MyImportBeanDefinitionRegistrar.class})

2.@Bean[����ĵ���������������]
    @Scope:����������
	 * prototype����ʵ���ģ�ioc��������������ȥ���÷�������������������С�
	 * 					ÿ�λ�ȡ��ʱ��Ż���÷�����������
	 * singleton����ʵ���ģ�Ĭ��ֵ����ioc������������÷�����������ŵ�ioc�����С�
	 * 			�Ժ�ÿ�λ�ȡ����ֱ�Ӵ�������map.get()�����ã�
	 * request��ͬһ�����󴴽�һ��ʵ��
	 * session��ͬһ��session����һ��ʵ��
	 * �����أ�
	 * 		��ʵ��bean��Ĭ��������������ʱ�򴴽�����
	 * 		�����أ������������������󡣵�һ��ʹ��(��ȡ)Bean�������󣬲���ʼ����
	@Scope("singleton")//Ĭ��ѡ��
	@Scope("prototype")
	@Lazy
	@Bean("person")//ָ��idΪperson
	@Bean //idĬ�����÷�����person01 
	public Person person01(){
		System.out.println("�����������Person....");
		return new Person("����", 25);
	}

3.@ComponentScan  value:ָ��Ҫɨ��İ�
Ĭ�ϰѣ�@Controller/@Service/@Repository/@Component����ʶ���������IOC����
@ComponentScan(value="com.annotation") 
@ComponentScan({"com.atguigu.service","com.atguigu.dao",
	"com.atguigu.controller","com.atguigu.bean"})
@ComponentScans(
		value = {
				@ComponentScan(value="com.atguigu",includeFilters = {
/*						@Filter(type=FilterType.ANNOTATION,classes={Controller.class}),
						@Filter(type=FilterType.ASSIGNABLE_TYPE,classes={BookService.class}),*/
						@Filter(type=FilterType.CUSTOM,classes={MyTypeFilter.class})
				},useDefaultFilters = false)	
		}
		)

  excludeFilters = Filter[] ��ָ��ɨ���ʱ����ʲô�����ų���Щ���
  includeFilters = Filter[] ��ָ��ɨ���ʱ��ֻ��Ҫ������Щ���
  FilterType.ANNOTATION������ע��
  FilterType.ASSIGNABLE_TYPE�����ո��������ͣ�
  FilterType.ASPECTJ��ʹ��ASPECTJ���ʽ
  FilterType.REGEX��ʹ������ָ��
  FilterType.CUSTOM��ʹ���Զ������
  
4. �Զ�װ��;
 * 		Spring��������ע�루DI������ɶ�IOC�������и��������������ϵ��ֵ��
 * 
 * 1����@Autowired���Զ�ע�룺
 * 		1����Ĭ�����Ȱ�������ȥ�������Ҷ�Ӧ�����:applicationContext.getBean(BookDao.class);�ҵ��͸�ֵ
 * 		2��������ҵ������ͬ���͵�������ٽ����Ե�������Ϊ�����idȥ�����в���
 * 							applicationContext.getBean("bookDao")
 * 		3����@Qualifier("bookDao")��ʹ��@Qualifierָ����Ҫװ��������id��������ʹ��������
 * 		4�����Զ�װ��Ĭ��һ��Ҫ�����Ը�ֵ�ã�û�оͻᱨ��
 * 			����ʹ��@Autowired(required=false);
 * 		5����@Primary����Spring�����Զ�װ���ʱ��Ĭ��ʹ����ѡ��bean��
 * 				Ҳ���Լ���ʹ��@Qualifierָ����Ҫװ���bean������
 * 		BookService{
 * 			@Autowired
 * 			BookDao  bookDao;
 * 		}
 * 
 * 2����Spring��֧��ʹ��@Resource(JSR250)��@Inject(JSR330)[java�淶��ע��]
 * 		@Resource:
 * 			���Ժ�@Autowiredһ��ʵ���Զ�װ�书�ܣ�Ĭ���ǰ���������ƽ���װ��ģ�
 * 			û����֧��@Primary����û��֧��@Autowired��reqiured=false��;
 * 		@Inject:
 * 			��Ҫ����javax.inject�İ�����Autowired�Ĺ���һ����û��required=false�Ĺ��ܣ�
 *  @Autowired:Spring����ģ� @Resource��@Inject����java�淶
 * 	
 * AutowiredAnnotationBeanPostProcessor:��������Զ�װ�书�ܣ�		
 * 
 * 3���� @Autowired:�����������������������ԣ����Ǵ������л�ȡ���������ֵ
 * 		1����[��ע�ڷ���λ��]��@Bean+���������������������л�ȡ;Ĭ�ϲ�д@AutowiredЧ����һ���ģ������Զ�װ��
 * 		2����[���ڹ�������]��������ֻ��һ���вι�����������вι�������@Autowired����ʡ�ԣ�����λ�õ�������ǿ����Զ��������л�ȡ
 * 		3�������ڲ���λ�ã�
 * 
 * 4�����Զ��������Ҫʹ��Spring�����ײ��һЩ�����ApplicationContext��BeanFactory��xxx����
 * 		�Զ������ʵ��xxxAware���ڴ��������ʱ�򣬻���ýӿڹ涨�ķ���ע����������Aware��
 * 		��Spring�ײ�һЩ���ע�뵽�Զ����Bean�У�
 * 		xxxAware������ʹ��xxxProcessor��
 * 			ApplicationContextAware==��ApplicationContextAwareProcessor��
 * 	

