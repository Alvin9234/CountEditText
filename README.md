# CountEditText
限制 EditText 的输入范围

maxLength：EditText的输入上限

textSize：EditText和显示字数的TextView的字体大小，在布局中声明使用时，textSize的单位要使用dp，因为 TypedArray.getDimension()方法是获取某个dimen的值,如果是dp或sp的单位,将其乘以density，字体会比常用控件中的设置大许多，所以代码中用的时候除以了density，不需要的可以自己改源码

hasClear:是否具有清除文本功能：true,具有；false,不具有

hint: 设置EditText的提示

bg_color: 设置EditText的背景

text_color: 设置EditText的文字颜色

hint_color: 设置EditText的提示文字颜色

count_color: 设置计数的提示文字颜色

调用示例：在引用布局中自定义命名空间
```Java
xmlns:app="http://schemas.android.com/apk/res-auto"

<包名.CountEditText
    	android:id="@+id/edit_count"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        app:maxLength="100"
        app:textSize="16dp"
        app:hasClear="true"
        app:hint="@string/feedback_hint"
    />
```

结果如下
![image](https://github.com/Alvin9234/CountEditText/blob/master/screenshoot/preview.jpg)
