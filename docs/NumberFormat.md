**NumberFormat** 类可以将一个数值格式化为符合某个国家地区习惯的数值字符串，也可以将符合某个国家地区习惯的数值字符串解析为对应的数值。

**NumberFormat方法**：

```
format 方法：将一个数值格式化为符合某个国家地区习惯的数值字符串
parse 方法：将符合某个国家地区习惯的数值字符串解析为对应的数值

实例化NumberFormat类（在实例化时，可以使用 Locale 对象作为参数，也可以不使用）：
getXXXInstance() 方法：返回NumberFormat实例对象
getInstance()：以默认语言环境返回NumberFormat实例对象

getNumberInstance(Locale locale):以参数Locale 对象所标识的本地信息来获得具有多种用途的NumberFormat实例对象(没什么实际作用)
getIntegerInstance(Locale locale):以参数locale对象所标识的本地信息来获得处理整数的NumberFormat实例对象(没什么实际作用)
getCurrencyInstance(Locale locale):以参数locale对象所标识的本地信息来获得处理货币的NumberFormat实例对象
getPercentInstance(Locale locale):以参数locale对象所标识的本地信息来获得处理百分比数值的NumberFormat实例对象
```

**DecimalFormat** 类是NumberFormat 十进制数字格式的具体子类。旨在**解析**和**格式化**任何语言环境中的数字，包括支持西方、阿拉伯语和印度语数字。

支持不同类型的数字，包括整数（123）、定点数（123.4）、科学计数法（1.23E4)，百分比（12%）和货币金额（123美元）。

对于数值的小数部分，默认显示**3**位小数，在去掉超出小数点3位以后的部分时，会将数值**四舍五入**为最接近的数值格式化输出。

**DecimalFormat特殊模式字符：**

| Symbol         | Location                            | Localized? | Meaning                                                      |
| :------------- | :---------------------------------- | :--------- | :----------------------------------------------------------- |
| `0`            | Number                              | Yes        | Digit<br />数字，被格式化数值不够的位数补零，若够则不变      |
| `#`            | Number                              | Yes        | Digit, zero shows as absent<br />数字，被格式化数值不够的位数忽略，若够则不变 |
| `.`            | Number                              | Yes        | Decimal separator or monetary decimal separator<br />小数分隔符或货币小数分隔符 |
| `-`            | Number                              | Yes        | Minus sign<br />减号,缺省负数前缀                            |
| `,`            | Number                              | Yes        | Grouping separator or monetary grouping separator<br />分组分隔符 |
| `E`            | Number                              | Yes        | Separates mantissa and exponent in scientific notation. *Need not be quoted in prefix or suffix.*<br />用科学记数法分隔尾数和指数。 不需要在前缀或后缀中引用。 |
| `;`            | Subpattern boundary<br />子模式边界 | Yes        | Separates positive and negative subpatterns<br />将正面和负面的子图案分开 |
| `%`            | Prefix or suffix<br />字首或字尾    | Yes        | Multiply by 100 and show as percentage<br />乘以100并显示为百分比 |
| `\u2030`       | Prefix or suffix                    | Yes        | Multiply by 1000 and show as per mille value<br />乘以1000并显示为千分数，显示出来为‰ |
| `¤` (`\u00A4`) | Prefix or suffix                    | No         | Currency sign, replaced by currency symbol. If doubled, replaced by international currency symbol. If present in a pattern, the monetary decimal/grouping separators are used instead of the decimal/grouping separators.<br />货币符号，由货币符号取代。如果加倍，则用国际货币符号代替。如果存在于模式中，则使用货币小数分隔符而不是小数分隔符。 |
| `'`            | Prefix or suffix                    | No         | Used to quote special characters in a prefix or suffix, for example, `"'#'#"` formats 123 to `"#123"`. To create a single quote itself, use two in a row: `"# o''clock"`.<br />用于引用前缀或后缀中的特殊字符，例如，"’#’#“格式为123到 “#123”。要创建单引号本身，请连续使用两个：”# o’'clock"。 |

**DecimalFormat常用成员方法**：

```
（1）applyPattern()方法，将给定的模式应用于Format对象
（2）getPositivePrefix()方法，获取正前缀
    getPositiveSuffix()方法，获取正后缀
    setPositivePrefix(String newValue)方法，设置正前缀     
    setPositiveSuffix(String newValue)方法，设置正后缀
（3）getNegativePrefix()方法，获取否前缀         
    getNegativeSuffix()方法，获取否后缀
	setNegativePrefix(String newValue)方法，设置否前缀      
    setNegativeSuffix(String newValue)方法，设置否后缀
（4）getCurrency()方法，获取格式化货币值时此十进制格式使用的货币。
（5）getGroupingSize()方法，返回分组大小。分组大小是数字的整数部分中分组分隔符之间的位数。例如，在数字“123,456.78”中，分组大小为3。
```

