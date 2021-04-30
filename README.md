# 简易工作流引擎

[sql脚本](https://github.com/shengchengchao/myApproval/blob/master/approval.sql)

## 目前已支持的部分：

### 支持的节点

1. 简单节点： 单节点
2. 会签节点： 一个大节点，里面有很多审批人，当这个大节点里的所有人都审批通过后，才能进入下一个节点
3. 并行节点：一个包含很多审批人的大节点，这个大节点里任何一个人审批通过，则该节点就完成

### 支持的操作

1. 节点的审批
2. 节点的驳回 只支持驳回到上一个节点
3. 节点的设置与展示

### 节点的状态

* Ready: 审批
* Complete: 审批完成
* Future: 待审批
* Waiting: 子节点待审批
* Rollback: 驳回
* Skip: 并行节点时 当前节点被跳过审批

## 如何使用

通过继承 `AbstractApprovalChain` ，并实现 `getVersion`方法，该方法是返回当前要进行审批的版本。通过该类调用对应方法就可以完成审批的通过与驳回。

考虑到在审批开始前要添加一些前置操作与后置操作，比如 提交任务，任务通过的提示。可以通过重写`AbstractApprovalChain` 的`addBeforeNode` 与`addAfterNode` 即可完成。

## 底层原理

整体是采用链表来构建。在项目初始化的时候，会进行审批节点处理的添加。节点的状态根据日志来进行判断。

顶级的抽象类为分为两个，AbstractNodeProcess 与 `AbstractLogProcess`  一个是处理节点有关的部分，一个来处理日志部分。

中间抽出来一个 抽象类 `CommonApprovalProcess` 来处理一些公共的逻辑部分，其下就是对于每种节点类型的处理。

![MyApproval类关系](https://cdn.jsdelivr.net/gh/shengchengchao/cloudPic@main/20210430151822.png)



