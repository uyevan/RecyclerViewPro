# RecyclerViewPro

[中文文档](https://github.com/uyevan/RecyclerViewPro/blob/master/README.CN.md)
> It's not too much to order a Start...

## Project Introduction

This project is called RecyclerViewPro, and it's like putting a set of superhero equipment on the
list feature of your Android app. Imagine you have an app with a lot of information to display, just
like a thick phone book, but you want users to browse it quickly and conveniently, and be able to
view the latest content at any time. RecyclerViewPro is here to fulfill that wish.

It combines RecyclerView with SmartRefreshLayout to implement advanced features, including
pull-to-refresh, load-more, asynchronous requests, and dynamic data updates, and more.

## Project Highlights

- Pull-to-refresh: Just as easy as turning on a tap to drink water, users can simply pull down the
  list to see the latest information flowing in, making it perfect for news apps or social media,
  ensuring that the content is always fresh.
- Load-more: Users reach the bottom of the list and want to see more? No problem, automatically load
  more content, just like a magician pulling rabbits out of a hat, ensuring that the user's
  exploration journey never ends.
- Asynchronous requests: This means working quietly in the background for you. When users scroll the
  list, the data is already on its way, without causing lag due to loading, providing an
  ultra-smooth user experience.
- Dynamic updates: Data changed? No problem, the list will intelligently update itself, only
  changing the parts that need to be changed, highly efficient and not wasting resources.

## Related Dependencies

To use these cool features, you'll need to add some secret weapons - dependency libraries - to your
project, such as:

- Lombok: Saves you from writing a bunch of annoying getters and setters, making coding as smooth as
  writing a diary.
- Glide: A little helper for image loading, it can display images quickly no matter how large they
  are, and it also saves bandwidth.
- SmartRefreshLayout: The magic behind pull-to-refresh and load-more, and it comes with various
  beautiful animation effects.

```gradle
    // LomBok
    implementation(libs.org.projectlombok.lombok4)
    annotationProcessor(libs.org.projectlombok.lombok4)
    // Glide
    implementation(libs.glide)
    // Refresh
    implementation(libs.refresh.layout.kernel)
    implementation(libs.github.refresh.header.classics)
    implementation(libs.refresh.footer.classics)
```

## Critical code

The following is the key code, please refer to the source code for other codes.

```java

/**
 * MainActivity操控
 */
@SuppressLint({"SimpleDateFormat", "NotifyDataSetChanged"})
public class MainActivity extends AppCompatActivity {

    /**
     * Handler消息队列处理器
     */
    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                //刷新
                case 1:
                    listAdapter.notifyDataSetChanged();//通知适配器数据集有变化需要重新渲染
                    if (smartRefreshLayout.isRefreshing())
                        smartRefreshLayout.finishRefresh(true);//数据重新渲染完后结束刷新View
                    break;
                //加载
                case 2:
                    int newDataItemListSize = msg.arg1;
                    boolean hasMoreData = newDataItemListSize == item;
                    if (hasMoreData) { //3
                        //TODO 可以加载更多
                        page++;
                        //listAdapter.notifyDataSetChanged();
                        /**
                         * 上下都一样只不过上面的会重新渲染整个数据集
                         * 下面测试新增的部分,显然性能更好
                         */
                        listAdapter.notifyItemRangeInserted(listAdapter.getItemCount(), listAdapter.getItemCount() + newDataItemListSize);
                        if (smartRefreshLayout.isLoading())
                            smartRefreshLayout.finishLoadMore(true);//数据重新渲染完后结束刷新View
                    } else { //4
                        //TODO 没有更多数据
                        listAdapter.notifyItemRangeInserted(listAdapter.getItemCount(), listAdapter.getItemCount() + newDataItemListSize);
                        //smartRefreshLayout.finishLoadMore(false);//数据重新渲染完后结束刷新View
                        //smartRefreshLayout.setNoMoreData(true);//设置为不能加载更多数据
                        /**
                         * 上下一样
                         */
                        if (smartRefreshLayout.isLoading())
                            smartRefreshLayout.finishLoadMoreWithNoMoreData();//完成加载并标记没有更多数据
                    }
                    break;
            }
            return true;
        }
    });
    /**
     * Handler消息
     */
    private Message messageRefresh = null;
    private Message messageLoadMore = null;
    /**
     * Recycler列表数据集
     */
    private List<Item> items;
    /**
     * Recycler列表适配器
     */
    private ListAdapter listAdapter;
    /**
     * Recycler列表
     */
    private RecyclerView listView;
    /**
     * Recycler列表布局管理器
     */
    private ListManager linearManager;
    /**
     * StartRefreshLayout
     */
    private SmartRefreshLayout smartRefreshLayout;
    /**
     * 定义两个请求参数分别是:
     * page:页码
     * item:每页的数据量
     * 对于刷新-->page = 1再去请求数据即可
     * 对于加载更多-->所以我们只需判断 实际返回的数据量 < item 就代表不能再下拉加载
     */
    private Integer page = 1;
    private final Integer item = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO 初始化成员与元素
        initView();
        initialize();

        //TODO 初始化上拉加载 | 下拉刷新
        initSmartRefreshLayoutData();

        //TODO 绑定Recycler列表Manager布局管理器
        listView.setLayoutManager(linearManager);

        //TODO 绑定Recycler列表Adapter适配器
        listView.setAdapter(listAdapter);

        //Recycler列表事件测试
        recyclerNoticeTest();
    }

    /**
     * 初始化成员变量
     */
    private void initialize() {
        items = new ArrayList<>();
        listAdapter = new ListAdapter(items);
        linearManager = new ListManager(getApplicationContext());

        //TODO StartRefreshLayout配置
        DateFormat dateFormat = new SimpleDateFormat("MM/dd日 HH:mm分");
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getApplicationContext())
                .setTimeFormat(dateFormat)
                .setAccentColor(Color.parseColor("#E91E63"))
        );
        //也可以自定义
        ClassicsFooter ballPulseFooter = new ClassicsFooter(getApplicationContext());
        ballPulseFooter.setAccentColor(Color.parseColor("#E91E63"));
        smartRefreshLayout.setRefreshFooter(ballPulseFooter);
    }

    /**
     * 初始化元素
     */
    private void initView() {
        listView = (RecyclerView) findViewById(R.id.recycler);
        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.smartRefreshLayout);
    }

    /**
     * StartRefreshLayout数据加载与刷新
     * 因为设计到数据加载与刷新，因此我们使用Handler消息机制
     */
    private void initSmartRefreshLayoutData() {
        //TODO 刷新数据
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                /**
                 * 这里处理第一页数据的重新获取比并渲染,步骤如下:
                 * 1.清空数据集
                 * 2.通知适配器
                 * 3.重新获取数据
                 * 4.发送消息给Handler
                 */
                items.clear(); //1
                listAdapter.notifyDataSetChanged();//2 或 listAdapter.notifyItemRangeRemoved(0, listAdapter.getItemCount());
                Item[] item = {
                        new Item(1, "AAA", "https://c-ssl.dtstatic.com/uploads/blog/202209/15/20220915110428_27433.thumb.1000_0.jpeg", "I am user 1."),
                        new Item(2, "BBB", "https://c-ssl.dtstatic.com/uploads/blog/202204/20/20220420020834_81266.thumb.1000_0.jpeg", "I am user 2."),
                        new Item(3, "CCC", "https://c-ssl.dtstatic.com/uploads/blog/202209/11/20220911082824_260cd.thumb.1000_0.jpg", "I am user 3."),
                        new Item(4, "DDD", "https://c-ssl.dtstatic.com/uploads/blog/202202/17/20220217232308_b7002.thumb.1000_0.jpeg", "I am user 4."),
                        new Item(5, "EEE", "https://c-ssl.dtstatic.com/uploads/blog/202207/15/20220715195302_8c03f.thumb.1000_0.jpg", "I am user 5.")

                };
                Collections.addAll(items, item);//3
                /*listAdapter.notifyDataSetChanged();//通知适配器数据集有变化需要重新渲染
                if(smartRefreshLayout.isRefreshing()) smartRefreshLayout.finishRefresh(true);//数据重新渲染完后结束刷新View*/

                messageRefresh = handler.obtainMessage(1);
                handler.sendMessage(messageRefresh); //4
            }
        });
        //TODO 加载数据
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                /**
                 * 这里处理更多加载数据,获取数据并判断是否还能更多加载,
                 * 如果能正常加载,否则设置成当前上下文下不可再加载;具体步骤如下:
                 * 1.获取新数据
                 * 2.判断是否还能更多加载(!=null || size()<pageItem)
                 * 3.能->追加新数据到数据集
                 * 4.不能->最后的新数据追加到数据集,设置为不能再上拉加载
                 * 5.发送Handler消息,处理
                 */

                /**
                 // 声明一个列表用于存储当前页面的数据
                 ArrayList<Item> newDataItemList = new ArrayList<>();
                 // 模拟网络请求加载数据，这里简化为两页数据
                 if (page == 1) {
                 newDataItemList.add(new Item(1, "AAA", "https://c-ssl.dtstatic.com/uploads/blog/202209/15/20220915110428_27433.thumb.1000_0.jpeg", "I am user 1."));
                 newDataItemList.add(new Item(2, "BBB", "https://c-ssl.dtstatic.com/uploads/blog/202204/20/20220420020834_81266.thumb.1000_0.jpeg", "I am user 2."));
                 newDataItemList.add(new Item(3, "CCC", "https://c-ssl.dtstatic.com/uploads/blog/202209/11/20220911082824_260cd.thumb.1000_0.jpg", "I am user 3."));
                 newDataItemList.add(new Item(4, "DDD", "https://c-ssl.dtstatic.com/uploads/blog/202202/17/20220217232308_b7002.thumb.1000_0.jpeg", "I am user 4."));
                 newDataItemList.add(new Item(5, "EEE", "https://c-ssl.dtstatic.com/uploads/blog/202207/15/20220715195302_8c03f.thumb.1000_0.jpg", "I am user 5."));
                 } else if (page == 2) {
                 newDataItemList.add(new Item(1, "AAA", "https://c-ssl.dtstatic.com/uploads/blog/202209/15/20220915110428_27433.thumb.1000_0.jpeg", "I am user 1."));
                 newDataItemList.add(new Item(2, "BBB", "https://c-ssl.dtstatic.com/uploads/blog/202204/20/20220420020834_81266.thumb.1000_0.jpeg", "I am user 2."));
                 newDataItemList.add(new Item(3, "CCC", "https://c-ssl.dtstatic.com/uploads/blog/202209/11/20220911082824_260cd.thumb.1000_0.jpg", "I am user 3."));
                 } //1 第一步(第一次正常,第二次不可再加载, 因为现在实际数据量=3,所以不能下拉加载)
                 */

                ArrayList<Item> newDataItemList = requestData();
                if (!newDataItemList.isEmpty()) {
                    items.addAll(newDataItemList);
                    /*boolean hasMoreData = newDataItemList.size() == item; //2
                    if (hasMoreData) { //3
                        //TODO 可以加载更多
                        page++;
                        //listAdapter.notifyDataSetChanged();
                        *//**
                     * 上下都一样只不过上面的会重新渲染整个数据集
                     * 下面测试新增的部分,显然性能更好
                     *//*
                        listAdapter.notifyItemRangeInserted(listAdapter.getItemCount(), listAdapter.getItemCount() + newDataItemList.size());
                        if(smartRefreshLayout.isRefreshing()) smartRefreshLayout.finishLoadMore(true);//数据重新渲染完后结束刷新View
                    } else { //4
                        //TODO 没有更多数据
                        listAdapter.notifyItemRangeInserted(listAdapter.getItemCount(), listAdapter.getItemCount() + newDataItemList.size());
                        //if(smartRefreshLayout.isLoading()) smartRefreshLayout.finishLoadMore(false);//数据重新渲染完后结束刷新View
                        //smartRefreshLayout.setNoMoreData(true);//设置为不能加载更多数据
                        *//**
                     * 上下一样
                     *//*
                        if(smartRefreshLayout.isLoading()) smartRefreshLayout.finishLoadMoreWithNoMoreData();//完成加载并标记没有更多数据
                    }*/

                    messageLoadMore = handler.obtainMessage(2, newDataItemList.size(), 0);
                    handler.sendMessage(messageLoadMore); //5
                    return;
                }
                //TODO 没有更多数据
                smartRefreshLayout.setEnableLoadMore(true);//关闭更多加载
            }
        });
    }

    /**
     * 模拟异步数据请求
     *
     * @return
     */
    private ArrayList<Item> requestData() {
        try {
            Callable<ArrayList<Item>> callable = new Callable<ArrayList<Item>>() {
                @Override
                public ArrayList<Item> call() throws Exception {
                    /**
                     * 这里进行真正的数据请求并最终返回Item模型对应的列表数据
                     * 实际上应该是Http请求
                     */
                    ArrayList<Item> newDataItemList = new ArrayList<>();
                    if (page == 1) {
                        newDataItemList.add(new Item(1, "AAA", "https://c-ssl.dtstatic.com/uploads/blog/202209/15/20220915110428_27433.thumb.1000_0.jpeg", "I am user 1."));
                        newDataItemList.add(new Item(2, "BBB", "https://c-ssl.dtstatic.com/uploads/blog/202204/20/20220420020834_81266.thumb.1000_0.jpeg", "I am user 2."));
                        newDataItemList.add(new Item(3, "CCC", "https://c-ssl.dtstatic.com/uploads/blog/202209/11/20220911082824_260cd.thumb.1000_0.jpg", "I am user 3."));
                        newDataItemList.add(new Item(4, "DDD", "https://c-ssl.dtstatic.com/uploads/blog/202202/17/20220217232308_b7002.thumb.1000_0.jpeg", "I am user 4."));
                        newDataItemList.add(new Item(5, "EEE", "https://c-ssl.dtstatic.com/uploads/blog/202207/15/20220715195302_8c03f.thumb.1000_0.jpg", "I am user 5."));
                    } else if (page == 2) {
                        newDataItemList.add(new Item(1, "AAA", "https://c-ssl.dtstatic.com/uploads/blog/202209/15/20220915110428_27433.thumb.1000_0.jpeg", "I am user 1."));
                        newDataItemList.add(new Item(2, "BBB", "https://c-ssl.dtstatic.com/uploads/blog/202204/20/20220420020834_81266.thumb.1000_0.jpeg", "I am user 2."));
                        newDataItemList.add(new Item(3, "CCC", "https://c-ssl.dtstatic.com/uploads/blog/202209/11/20220911082824_260cd.thumb.1000_0.jpg", "I am user 3."));
                    }
                    return newDataItemList;
                }
            };
            //异步计算器来包装Callable
            FutureTask<ArrayList<Item>> futureTask = new FutureTask<>(callable);
            //创建新线程
            Thread requestThread = new Thread(futureTask);
            requestThread.start();

            //TODO 模拟请求耗时 1 秒
            Thread.sleep(1000);

            //等待线程完成并返回数据
            return futureTask.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Recycler列表事件测试
     */
    private void recyclerNoticeTest() {
        // 添加多个元素
        Button initList = findViewById(R.id.init_list_button);
        initList.setOnClickListener(v -> {
            Item[] item = {
                    new Item(1, "AAA", "https://c-ssl.dtstatic.com/uploads/blog/202209/15/20220915110428_27433.thumb.1000_0.jpeg", "I am user 1."),
                    new Item(2, "BBB", "https://c-ssl.dtstatic.com/uploads/blog/202204/20/20220420020834_81266.thumb.1000_0.jpeg", "I am user 2."),
                    new Item(3, "CCC", "https://c-ssl.dtstatic.com/uploads/blog/202209/11/20220911082824_260cd.thumb.1000_0.jpg", "I am user 3."),
                    new Item(4, "DDD", "https://c-ssl.dtstatic.com/uploads/blog/202202/17/20220217232308_b7002.thumb.1000_0.jpeg", "I am user 4."),
                    new Item(5, "EEE", "https://c-ssl.dtstatic.com/uploads/blog/202207/15/20220715195302_8c03f.thumb.1000_0.jpg", "I am user 5.")

            };
            //TODO 以下三种方式均可以
            /**
             * 尾插
             */
            items.addAll(items.size(), Arrays.asList(item));
            listAdapter.notifyDataSetChanged();//通知Adapter适配器列表数据集有变化
            listAdapter.notifyItemRangeChanged(items.size(), listAdapter.getItemCount());
            listAdapter.notifyItemRangeChanged(items.size(), item.length);
            /**
             * 头插
             */
            //items.addAll(1, Arrays.asList(item));
            //listAdapter.notifyItemInserted(1);
            //listAdapter.notifyItemRangeInserted(1, item.length + 1);//通知Adapter适配器列表有插入数据集
            //或
            //listAdapter.notifyItemRangeChanged(1, items.size() + 1);
        });
        // 添加单个元素
        Button addOne = findViewById(R.id.add_one_button);
        addOne.setOnClickListener(v -> {
            //TODO 以下三种方式均可以
            /**
             * 头插
             */
            //items.add(new Item(6, "FFF", "https://c-ssl.dtstatic.com/uploads/blog/202203/19/20220319205139_b509f.thumb.1000_0.jpg", "I am user 6."));
            //listAdapter.notifyDataSetChanged();//通知Adapter适配器列表数据集有变化
            //listAdapter.notifyItemRangeChanged(items.size(), listAdapter.getItemCount());
            //listAdapter.notifyItemRangeChanged(items.size(), items.size() + item.length);
            /**
             * 尾插
             */
            items.add(1, new Item(6, "FFF", "https://c-ssl.dtstatic.com/uploads/blog/202203/19/20220319205139_b509f.thumb.1000_0.jpg", "I am user 6."));
            listAdapter.notifyItemInserted(1);
            listAdapter.notifyItemRangeChanged(1, items.size() + 1);
        });
        // 添加多个元素
        Button addMore = findViewById(R.id.add_more_button);
        addMore.setOnClickListener(v -> {
            Item[] item = {
                    new Item(7, "JJJ", "https://c-ssl.dtstatic.com/uploads/blog/202201/23/20220123222213_2899a.thumb.1000_0.jpeg", "I am user 7."),
                    new Item(8, "HHH", "https://c-ssl.dtstatic.com/uploads/blog/202205/20/20220520210602_7c3ba.thumb.1000_0.jpeg", "I am user 8."),
                    new Item(9, "III", "https://c-ssl.dtstatic.com/uploads/blog/202205/20/20220520210604_2423f.thumb.1000_0.jpeg", "I am user 9.")
            };
            Collections.addAll(items, item);
            //TODO 以下三种方式均可以
            //listAdapter.notifyDataSetChanged();//通知Adapter适配器列表数据集有变化
            //listAdapter.notifyItemRangeChanged(items.size(), listAdapter.getItemCount());
            listAdapter.notifyItemRangeChanged(items.size(), items.size() + item.length);
        });


        // 移除单个元素
        Button removeOne = findViewById(R.id.remove_one_button);
        removeOne.setOnClickListener(v -> {
            //移除第二个元素
            items.remove(1);
            listAdapter.notifyItemRemoved(1);
            //TODO 通知受影响的item都刷新position
            listAdapter.notifyItemRangeChanged(1, listAdapter.getItemCount() - 1);
        });

        // 移除多个元素
        Button removeMore = findViewById(R.id.remove_more_button);
        removeMore.setOnClickListener(v -> {
            //移除第三，第四个元素
            items.remove(2);
            listAdapter.notifyItemRemoved(2);
            items.remove(3);
            listAdapter.notifyItemRemoved(3);
            //TODO 通知受影响的item都刷新position
            listAdapter.notifyItemRangeChanged(2, listAdapter.getItemCount() - 1);
        });

        // 清空全部元素
        Button removeAll = findViewById(R.id.remove_all_button);
        removeAll.setOnClickListener(v -> {
            //移除全部
            items.clear();
            listAdapter.notifyDataSetChanged();//通知Adapter适配器列表数据集有变化
        });


        // 更新单个元素
        Button refreshOne = findViewById(R.id.refresh_one_button);
        refreshOne.setOnClickListener(v -> {
                    //更新第二个元射
                    Item item = new Item(3, "CCC", "https://c-ssl.dtstatic.com/uploads/blog/202209/11/20220911082824_260cd.thumb.1000_0.jpg", "I am user 3.");
                    /**
                     * 根系数据集才能对列表有效果 | 因为Adapter是通过适配器来渲染数据
                     */
                    items.set(2, item);
                    listAdapter.notifyItemChanged(2, item);
                }
        );
        // 更新多个元素
        Button refreshMore = findViewById(R.id.refresh_more_button);
        refreshMore.setOnClickListener(v -> {
            items.get(1).setName("Updated");
            items.get(3).setHead("https://c-ssl.dtstatic.com/uploads/blog/202201/23/20220123222213_2899a.thumb.1000_0.jpeg");
            /**
             * 此方法会重新渲染1到3之间的三个Item元素 | 缺点:不更新的也重新渲染
             */
            //listAdapter.notifyItemRangeChanged(1, 3);
            /**
             * 此方法只更新所指定的Item元素
             */
            listAdapter.notifyItemChanged(1);
            listAdapter.notifyItemChanged(3);
        });

        // 更新全部元素
        Button refreshAll = findViewById(R.id.refresh_all_button);
        refreshAll.setOnClickListener(v -> {
            items.forEach(item -> {
                item.setProfile("Update all items.");
            });
            listAdapter.notifyDataSetChanged();//通知Adapter适配器列表数据集有变化
        });


        // 更换元素
        Button moved = findViewById(R.id.moved_button);
        moved.setOnClickListener(v -> {
            //更换第二个和第四个元射位置
            //TODO 注意先移除小表大的元射
            Item item_3 = items.remove(3);
            Item item_1 = items.remove(1);
            items.add(1, item_3);
            items.add(3, item_1);
            listAdapter.notifyItemMoved(3, 1);
            listAdapter.notifyItemRangeChanged(1, 3); //更新下标1到3的元素
        });

        // 更换元素
        Button update = findViewById(R.id.changed_button);
        update.setOnClickListener(v -> {
            listAdapter.notifyDataSetChanged();//通知Adapter适配器列表数据集有变化
            Toast.makeText(this, "已发送notify()", Toast.LENGTH_SHORT).show();
        });
    }
}

/**
 * ListAdapter适配器
 */
public class ListAdapter extends RecyclerView.Adapter<ItemViewHolder> {

  /**
   * 数据集 itemList
   */
  private List<Item> itemList;

  /**
   * 子项View视图
   */
  private View view;

  /**
   * 子项Item模型
   */
  private Item item;

  private ListAdapter() {
  }

  public ListAdapter(List<Item> itemList) {
    this.itemList = itemList;
  }

  /**
   * 构造View视图缓存器ItemViewHolder
   *
   * @param parent   父视图
   * @param viewType 视图类型
   * @return
   */
  @NonNull
  @Override
  @SuppressLint("InflateParams")
  public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    //TODO 创建缓存器并传递给一个View视图
    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
    /*view = View.inflate(parent.getContext(), R.layout.item_list, null); 通过View.inflate无法宽度最大化*/
    return new ItemViewHolder(view);
  }

  /**
   * 绑定数据到ItemViewHolder视图缓存器的每一个Item
   *
   * @param holder   子项Item对应的View视图
   * @param position 子项Item在数据集里的位置
   */
  @Override
  @SuppressLint("DefaultLocale")
  public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
    //TODO 获取第 position 个子项
    item = itemList.get(position);
    //TODO 设置Holder对应的视图数据
    holder.id.setText(String.valueOf(item.getId()));
    holder.name.setText(item.getName());
    Glide.with(view.getContext()).load(item.getHead()).into(holder.head);
    holder.profile.setText(String.format("%s | POSITION:%d", item.getProfile(), position));
  }

  /**
   * 数据集里面的总项数
   *
   * @return itemList.size() - 1
   */
  @Override
  public int getItemCount() {
    return itemList != null ? itemList.size() : 0;
  }
}
```

## Check out the Effect

Do you want to witness the miracle with your own eyes? Here's a screenshot and a small video
demonstration, like a teaser trailer for the project, allowing you to experience the silky smooth
operation and instant updates in advance.

> Project Screenshot

<img alt="Demo" height="500" src="https://img2.imgtp.com/2024/06/01/SD4iyI04.png" width="1024"/>

> Run Demo

<img alt="Demo" height="500" src="https://img2.imgtp.com/2024/06/01/XRcuwOYn.gif" width="1024"/>

