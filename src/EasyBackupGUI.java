import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * 列表视图的父类。继承此类，可以实现一种文件类型的显示列表UI。
 */
class UIListLayout {
    private Display display;

    // UI布局设置
    private RowLayout layout;
    private final int spacing = 5;      // 列表项目排列的间距（单位：像素）
    private final int itemWidth = 90;   // 单个项目的宽度
    private final int itemHeight = 90;  // 单个项目的高度

    // 滚动组件
    private ScrolledComposite scrollPanel;      // 滚动面板
    private Composite thumbsPanel;              // 显示缩略图的面板

    /**
     * 初始化布局组件。
     */
    void initLayout(Display display, Shell window) {
        this.display = display;

        // 初始化UI界面组件
        scrollPanel = new ScrolledComposite(window, SWT.BORDER | SWT.V_SCROLL);     // 滚动条面板
        scrollPanel.setExpandHorizontal(true);
        scrollPanel.setExpandVertical(true);

        thumbsPanel = new Composite(scrollPanel, SWT.BORDER);                           // 排列面板
        scrollPanel.setContent(thumbsPanel);

        // 初始化UI布局设置
        window.setLayout(new FormLayout());     // 根窗口使用FormLayout进行布局，用于在其上布局缩略图面板等组件
        // （展示列表的面板始终距离窗口上下左右的边缘10像素）
        FormData fdScrollPanel = new FormData();
        fdScrollPanel.left = new FormAttachment(0, 10);
        fdScrollPanel.right = new FormAttachment(100, -10);
        fdScrollPanel.top = new FormAttachment(0, 10);
        fdScrollPanel.bottom = new FormAttachment(100, -60);
        scrollPanel.setLayoutData(fdScrollPanel);
        window.layout();

        // 设置监听器，在窗口大小改变时自动调整列表项目的排布
        window.addListener(SWT.Resize, event -> {
            thumbsPanel.layout();
            int thumbsWidth = scrollPanel.getClientArea().width;
            int thumbsHeight = thumbsPanel.computeSize(thumbsWidth, SWT.DEFAULT).y;
            scrollPanel.setMinSize(thumbsWidth, thumbsHeight);
        });

        // 初始化面板的布局，用于排列项目
        RowLayout thumbsLayout = new RowLayout(SWT.HORIZONTAL);
        thumbsLayout.spacing = spacing;
        thumbsLayout.wrap = true;
        thumbsPanel.setLayout(thumbsLayout);
    }


    /**
     * 向列表视图中添加一个新的项目。
     * @param iconPath 图标文件的路径
     */
    void addItem(String iconPath) {
        // 缩略图图标对象
        Image image = new Image(display, iconPath);

        // 显示
        Label ListItem = new Label(thumbsPanel, SWT.BORDER);

        // 指定布局数据（指定每个项目的长和宽）
        ListItem.setLayoutData(new RowData(itemWidth, itemHeight));
        thumbsPanel.layout();

        // 添加渲染监听器，用于按照ListItem的大小加载缩略图像
        ListItem.addPaintListener(paintEvent -> paintEvent.gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 0, 0, ListItem.getSize().x, ListItem.getSize().y));

        // 每添加一个项目，就重新计算一次面板的高度，
        // 项目填满一行后自动换行，排列到到底部溢出窗口后会显示竖向滚动条
        int thumbsWidth = scrollPanel.getClientArea().width;
        int thumbsHeight = thumbsPanel.computeSize(thumbsWidth, SWT.DEFAULT).y;
        scrollPanel.setMinSize(thumbsWidth, thumbsHeight);
    }

}

/**
 * EasyBackup软件主程序类，负责UI布局初始化、调用其他类。
 */
public class EasyBackupGUI {
    private final Display display;
    private final Shell shell;

    public EasyBackupGUI(String title, int[] size) {
        // 初始化UI
        display = new Display();
        shell = new Shell(display);

        // 配置UI
        shell.setText(title);               // 设置窗口标题
        shell.setSize(size[0], size[1]);    // 设置窗口大小

        initUI();
    }

    /**
     * 加载程序界面
     */
    private void initUI() {
        // 设置布局方案（使用FormLayout排列UI组件）
        shell.setLayout(new FormLayout());

        // 欢迎文字
        Label textInfo = new Label(shell, SWT.NONE | SWT.CENTER);
        textInfo.setFont(new Font(display, "微软雅黑", 18, SWT.BOLD));
        textInfo.setText("欢迎使用EasyBackup！");

        FormData fdTextInfo = new FormData();
        fdTextInfo.left = new FormAttachment(0, 120);
        fdTextInfo.right = new FormAttachment(100, -120);
        fdTextInfo.top = new FormAttachment(0, 80);
        textInfo.setLayoutData(fdTextInfo);

        // 操作按钮
        Button buttonStart = new Button(shell, SWT.PUSH);
        buttonStart.setText("开始连接手机");

        FormData fdButtonStart = new FormData();
        fdButtonStart.top = new FormAttachment(textInfo, 30, SWT.BOTTOM);
        fdButtonStart.left = new FormAttachment(textInfo, 0, SWT.LEFT);
        fdButtonStart.right = new FormAttachment(textInfo, 0, SWT.RIGHT);
        buttonStart.setLayoutData(fdButtonStart);
    }

    /**
     * 启动主程序并阻塞，直到程序退出。
     */
    public void run() {
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }


    static void main() {
        // 创建并配置主程序的实例
        EasyBackupGUI mainWindow = new EasyBackupGUI(
                "EasyBackup - Internal test",
                new int[] {1024, 768});

        mainWindow.run();
    }
}
