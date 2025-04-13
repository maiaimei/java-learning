在终端中运行以下命令，使用 `pip` 安装 `requirements.txt` 中列出的依赖库：

```shell
pip install -r requirements.txt
```

如果需要更新依赖库版本，可以手动编辑 `requirements.txt` 或重新运行 `pip freeze`。

```shell
pip freeze > requirements.txt
```

在终端运行脚本：

```shell
python extract_jep_component.py

python extract_jep_information.py "C:/Users/lenovo/Desktop/jeps.txt" "JDK 24"
```

