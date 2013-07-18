<%@ page import="pub.platform.db.ConnectionManager" %>
<%@ page import="pub.platform.db.DatabaseConnection" %>
<%@ page import="pub.platform.db.RecordSet" %>
<%@ page import="java.net.Inet4Address" %>
<%@ page import="java.net.InetAddress" %>
<%@ page import="java.net.NetworkInterface" %>
<%@ page import="java.net.SocketException" %>
<%@ page import="pub.platform.advance.utils.PropertyManager" %>
<%@ page contentType="text/html; charset=GBK" %>
<%@ include file="/pages/security/loginassistor.jsp" %>
<%
    response.setContentType("text/html; charset=GBK");
    String contextPath = request.getContextPath();

    /*2011-4-7 Cookie设置添加到loginassistor.jsp中*/
    String jsonDefaultMenu = null;
    String jsonSystemMenu = null;
    //OperatorManager om = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    try {
        //xmlString =om.getXmlString();
        jsonDefaultMenu = om.getJsonString("default");
        jsonSystemMenu = om.getJsonString("system");
        //System.out.println( om.getOperator().getPtDeptBean().getSuperdqdm());
        //System.out.println( om.getOperator().getPtDeptBean().getSuperdqmc());
    } catch (Exception e) {
        System.out.println("jsp" + e + "\n");
    }
    String deptname = "";
    String operid = "";

    String rolesall = null;

    if (om != null) {
        if (om.getOperator() != null) {
            username = om.getOperatorName();
            operid = om.getOperator().getOperid();
            if (om.getOperator().getPtDeptBean() != null)
                deptname = om.getOperator().getPtDeptBean().getDeptname();

            //角色
            List roles = new ArrayList();
            DatabaseConnection conn = ConnectionManager.getInstance().get();
            RecordSet rs = conn.executeQuery("select a.roledesc from ptoperrole b right join ptrole a on b.roleid = a.roleid  where b.operid='" + operid + "'");
            while (rs.next()) {
                roles.add(rs.getString("roledesc"));
            }
            ConnectionManager.getInstance().release();
            rolesall = " ";
            for (int i = 0; i < roles.size(); i++) {
                rolesall += roles.get(i) + " ";
            }
        }
    }
%>
<script type="text/javascript">
    var g_jsContextPath = "<%=contextPath%>";
</script>

<html>
<head>
<title>青岛市财政非税代收代理系统</title>
<meta content="mshtml 6.00.2800.1106" name="generator">
<LINK href="<%=contextPath%>/css/diytabbar.css" type="text/css" rel="stylesheet">
<LINK href="<%=contextPath%>/dhtmlx/codebase/dhtmlxlayout.css" type="text/css" rel="stylesheet">
<LINK href="<%=contextPath%>/dhtmlx/codebase/skins/dhtmlxlayout_dhx_skyblue.css" type="text/css" rel="stylesheet">
<script language="javascript" src="<%=contextPath%>/dhtmlx/codebase/dhtmlxcommon.js"></script>
<script language="javascript" src="<%=contextPath%>/dhtmlx/codebase/dhtmlxlayout.js"></script>
<script language="javascript" src="<%=contextPath%>/dhtmlx/codebase/dhtmlxcontainer.js"></script>
<link rel="stylesheet" type="text/css" href="../../dhtmlx/dhtmlxTabbar/codebase/dhtmlxtabbar.css"/>
<script src="../../dhtmlx/dhtmlxTabbar/codebase/dhtmlxtabbar.js" type="text/javascript"></script>
<LINK href="<%=contextPath%>/dhtmlx/codebase/skins/dhtmlxaccordion_dhx_skyblue.css" type="text/css" rel="stylesheet">
<script language="javascript" src="<%=contextPath%>/dhtmlx/codebase/dhtmlxaccordion.js"></script>
<LINK href="<%=contextPath%>/dhtmlx/codebase/dhtmlxtree.css" type="text/css" rel="stylesheet">
<script language="javascript" src="<%=contextPath%>/dhtmlx/codebase/dhtmlxtree.js"></script>
<script language="javascript" src="<%=contextPath%>/dhtmlx/codebase/ext/dhtmlxtree_json.js"></script>
<%--<script type="text/javascript" src="homePage.js"></script>--%>
<style type="text/css">
    html, body {
        margin: 0px;
        width: 100%;
        height: 100%;
        padding: 0px;
        overflow: hidden;
    }

    .divlayout {
        position: relative;
        top: 0px;
        left: 0px;
        width: 100%;
        height: 100%;
        margin: 0px;
        padding: 0px;
        overflow: hidden;
    }

    .headfont {
        font-size: 12px;
        font-family: SimSun;
        color: #7387A0;
    }

    .skin-top-right {
        /*top: 0;*/
        /*right: 0;*/
        /*z-index: 7;*/
        /*height: 90px;*/
        /*position: absolute;*/
        /*width: 100%;*/
        background-position: top right;
        background-repeat: no-repeat;
        background-image: url(../../images/top_right.jpg)
    }
</style>
<script type="text/javascript">
    var defaultMenuStr = '<%=jsonDefaultMenu%>';
    var systemMenuStr = '<%=jsonSystemMenu%>';
    var dhxLayout;
    var bizdhxLayout;
    var bizdhxAccord;
    var biztabbar;
    var sysdhxLayout;
    var sysdhxAccord;
    var systabbar;
    function doOnLoad() {
        bizdhxLayout = new dhtmlXLayoutObject("bizlayout", "2U", "dhx_skyblue");
        doBizLoad();
        sysdhxLayout = new dhtmlXLayoutObject("syslayout", "2U", "dhx_skyblue");
        doSysLoad();
        tabbarhide("bizlayout");
        document.getElementById("biz").setAttribute("active", "true");
        document.getElementById("biz").setAttribute("className", "tabs-item-active");
    }

    function doBizLoad() {
        bizdhxLayout.cells("a").setWidth(212);
        bizdhxLayout.cells("a").hideHeader();
        bizdhxLayout.cells("b").hideHeader();
        bizdhxLayout.setAutoSize("a;b", "a;b");
        bizdhxAccord = bizdhxLayout.cells("a").attachAccordion();
        biztabbar = bizdhxLayout.cells("b").attachTabbar();

        biztabbar.setSkin("dhx_skyblue");
        biztabbar.setImagePath("../../dhtmlx/dhtmlxTabbar/codebase/imgs/");
        biztabbar.setHrefMode("iframes-on-demand");
        biztabbar.setSkinColors("#FCFBFC", "#F4F3EE");
        biztabbar.enableTabCloseButton(true);
        biztabbar.addTab("a1", "首页", "100px");
        biztabbar.setContentHref("a1", "trackMisc.xhtml");
        biztabbar.setTabActive("a1");
        biztabbar.attachEvent("onSelect", function (id, last_id) {
            biztree.selectItem(id);
            if (id != last_id)
                document.getElementById("lasttabdivid").value = last_id;
            return true;
        });
        biztabbar.attachEvent("onTabClose", function () {
            biztabbar.setTabActive(document.getElementById("lasttabdivid").value);
            return true;
        });
        bizdhxAccord.setSkin("dhx_skyblue");
        bizdhxAccord.setIconsPath("<%=contextPath%>/dhtmlx/codebase/icons/");
        bizdhxAccord.addItem("a1", "业务功能");
        bizdhxAccord.addItem("a2", "常用功能");
        bizdhxAccord.addItem("a3", "待办事项");
        bizdhxAccord.addItem("a4", "实时消息");

        bizdhxAccord.openItem("a1");
        bizdhxAccord._enableOpenEffect = true;
        bizdhxAccord.cells("a1").setIcon("accord_biz.png");
        bizdhxAccord.cells("a2").setIcon("accord_manage.png");
        bizdhxAccord.cells("a3").setIcon("editor.gif");
        bizdhxAccord.cells("a4").setIcon("accord_support.png");

        var biztree = bizdhxAccord.cells("a1").attachTree();
        var treeDefaultJson = eval('(' + defaultMenuStr + ')');
        biztree.setSkin('dhx_skyblue');
        biztree.setImagePath("<%=contextPath%>/dhtmlx/codebase/imgs/csh_books/");
        //biztree.enableDragAndDrop(true);
        biztree.loadJSONObject(treeDefaultJson);
        biztree.attachEvent("onClick", function (id) {
            var action = (biztree.getUserData(id, "url"));
            if (action == "#") {
                biztree.openItem(id);
            } else {
                var text = biztree.getSelectedItemText();
                bizaddtabbar(id, text, "<%=contextPath%>" + action);
            }
            return true;
        });
    }
    function doSysLoad() {
        sysdhxLayout.cells("a").setWidth(200);
        sysdhxLayout.cells("a").hideHeader();
        sysdhxLayout.cells("b").hideHeader();
        sysdhxAccord = sysdhxLayout.cells("a").attachAccordion();
        systabbar = sysdhxLayout.cells("b").attachTabbar();

        systabbar.setSkin("dhx_skyblue");
        systabbar.setImagePath("../../dhtmlx/dhtmlxTabbar/codebase/imgs/");
        systabbar.setHrefMode("iframes-on-demand");
        systabbar.setSkinColors("#FCFBFC", "#F4F3EE");
        systabbar.enableTabCloseButton(true);
        systabbar.addTab("a1", "首页", "100px");
        systabbar.setContentHref("a1", "trackMisc.xhtml");
        systabbar.setTabActive("a1");
        systabbar.attachEvent("onSelect", function (id, last_id) {
            managetree.selectItem(id);
            return true;
        });
        sysdhxAccord.setSkin("dhx_skyblue");
        sysdhxAccord.setIconsPath("<%=contextPath%>/dhtmlx/codebase/icons/");
        sysdhxAccord.addItem("a1", "系统管理");
        sysdhxAccord.openItem("a1");
        sysdhxAccord._enableOpenEffect = true;
        sysdhxAccord.cells("a1").setIcon("accord_manage.png");
        var managetree = sysdhxAccord.cells("a1").attachTree();
        var treeSystemJson = eval('(' + systemMenuStr + ')');
        managetree.setSkin('dhx_skyblue');
        managetree.setImagePath("<%=contextPath%>/dhtmlx/codebase/imgs/csh_books/");
        managetree.loadJSONObject(treeSystemJson);
        managetree.attachEvent("onClick", function (id) {
            var action = (managetree.getUserData(id, "url"));
            if (action == "#") {
                managetree.openItem(id);
            } else {
                var text = managetree.getSelectedItemText();
                sysaddtabbar(id, text, "<%=contextPath%>" + action);
            }
            return true;
        });
    }

    function bizaddtabbar(divID, tabname, url) {
        var tabbarCell = biztabbar.cells(divID);
        if (tabbarCell == undefined) {
            biztabbar.addTab(divID, tabname, "*");
            biztabbar.setContentHref(divID, url);
            biztabbar.setTabActive(divID);
        } else {
            biztabbar.setTabActive(divID);
            biztabbar.forceLoad(divID, url);
        }
    }
    function sysaddtabbar(divID, tabname, url) {
        var tabbarCell = systabbar.cells(divID);
        if (tabbarCell == undefined) {
            systabbar.addTab(divID, tabname, "*");
            systabbar.setContentHref(divID, url);
            systabbar.setTabActive(divID);
        } else {
            systabbar.setTabActive(divID);
            systabbar.forceLoad(divID, url);
        }
    }
    var layoutary = new Array('bizlayout', 'syslayout', 'helplayout', 'verlayout');

    function tabbarhide(tabbarid) {
        for (var i = 0; i < layoutary.length; i++) {
            if (layoutary[i] != tabbarid) {
                document.getElementById(layoutary[i]).style.display = "none";
            }
        }
    }
    function tabbarclk(obj) {
        var active = obj.getAttribute("active");
        if (active == 'false') {
            var tabbarid = obj.getAttribute("id") + "layout";
            setclass(obj.getAttribute("id"));
            obj.setAttribute("active", "true");
            obj.setAttribute("className", "tabs-item-active");
            document.getElementById(tabbarid).style.display = "inline";
            tabbarhide(tabbarid);
        }
    }
    var tabbarary = new Array('biz', 'sys', 'help', 'ver');
    function setclass(activeid) {
        for (var i = 0; i < tabbarary.length; i++) {
            if (tabbarary[i] != activeid) {
                document.getElementById(tabbarary[i]).setAttribute("className", "tabs-item");
                document.getElementById(tabbarary[i]).setAttribute("active", "false");
            }
        }
    }
    function Relogin() {
        parent.window.reload = "true";
        parent.window.location.replace("<%=contextPath%>/pages/security/logout.jsp");
    }
    function changepwd() {
        var sfeature = "dialogwidth:400px; dialogheight:200px;center:yes;help:no;resizable:no;scroll:no;status:no";
        window.showModalDialog("<%=contextPath%>/UI/system/deptUser/Passwordedit.jsp", "test", sfeature);
    }

    function doOnResize() {
        bizdhxLayout.setSizes();
        sysdhxLayout.setSizes();
    }
</script>
</head>

<body onload="doOnLoad()" onResize="doOnResize();">
<%--关闭tab时返回上一个浏览的tab--%>
<input type="hidden" id="lasttabdivid">

<div class="skin-top-right">
    <table width="100%" cellpadding="0" cellspacing="0" style="margin:0px;padding:0px;height:100%;">
        <tr width="100%" height="35px">
            <td width="5%" rowspan="2">
                &nbsp;
                <img src="../../images/logo.jpg" height="50px">
            </td>
            <td colspan="2">
                <img src="../../images/title3.jpg" height="40px" style="margin-left: 5px">
            </td>
            <td style="height:25px;text-align:right" class="headfont">
                <span>您好,<%=username%> </span>
                <span><%= " | <" + rolesall + "> |" %></span>
            <span onclick="changepwd()"
                  onMouseOver="this.style.cursor='hand'">修改密码</span>
             <span onclick="Relogin()"
                   onMouseOver="this.style.cursor='hand'">| 退出&nbsp;&nbsp;</span>
            </td>
        </tr>
        <tr width="100%" height="25px">
            <td width="2%"></td>
            <td colspan="2" style="height:25px;">
                <div onclick="tabbarclk(this);" active="true" id="biz" class="tabs-item-active"
                     style="float:left;width:80px;">
                    <span style="width:100%;">业务操作</span>
                </div>
                <div style="float:left;width:2px;"></div>
                <div onclick="tabbarclk(this);" active="false" id="sys" class="tabs-item"
                     style="float:left;width:80px;">
                    <span style="width:100%;">系统管理</span>
                </div>
                <div style="float:left;width:2px;"></div>
                <div onclick="tabbarclk(this);" active="false" id="help" class="tabs-item"
                     style="float:left;width:80px;">
                    <span style="width:100%;">操作帮助</span>
                </div>
                <div style="float:left;width:2px;"></div>
                <div onclick="tabbarclk(this);" active="false" id="ver" class="tabs-item"
                     style="float:left;width:80px;">
                    <span style="width:100%;">版本历史</span>
                </div>
                <div align="right" class="headfont">
                    <%--<%=" " + deptname + " | " + operid + " | <" + rolesall + ">" %>--%>
                </div>
            </td>
        </tr>
        <tr width="100%" height="4px">
            <td width="100%" style="height:4px;background-color: #3169AD;" colspan="4"></td>
        </tr>
        <tr width="100%">
            <td width="100%" colspan="4">
                <div class="divlayout" id="bizlayout"></div>
                <div class="divlayout" id="syslayout"></div>
                <div class="divlayout" id="helplayout">
                    </br>系统帮助信息...
                </div>
                <div class="divlayout" id="verlayout">
                    </br>版本更新历史...
                </div>
            </td>
        </tr>
    </table>
</div>
</body>

</html>