<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
+ request.getServerName() + ":" + request.getServerPort()
+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
  <base href="<%=basePath%>">

  <title>DDB-SQL</title>

  <meta http-equiv="pragma" content="no-cache">
  <meta http-equiv="cache-control" content="no-cache">
  <meta http-equiv="expires" content="0">
  <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
  <meta http-equiv="description" content="This is my page">
<!--
 <link rel="stylesheet" type="text/css" href="styles.css">
-->

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" href="tree_js/jquery.treeview.css" />
<!-- <link rel="stylesheet" href="tree_js/screen.css" /> -->
<link rel="stylesheet" type="text/css" href="<%=path%>/styles.css" />

        <!--[if IE]>
        
        <style type="text/css">
        .clear {
          zoom: 1;
          display: block;
        }
        </style>

        
        <![endif]-->
        
      </head>

      <body>

        <div class="section" id="page"> <!-- Defining the #page section with the section tag -->

          <div class="header"> <!-- Defining the header section of the page with the appropriate tag -->

            <h1>DDB-SQL</h1>
            <h3>Effective SQL IN DDBS</h3>

            <div class="nav clear"> <!-- The nav link semantically marks your main site navigation -->
              <ul>
                <li><a href="#article1">Introduction</a></li>
                <li><a href="#article2">SQL-Online</a></li>
                <li><a href="#article3">Summary</a></li>
              </ul>
            </div>

          </div>

          <div class="section" id="articles"> <!-- A new section with the articles -->

            <!-- Article 1 start -->

            <div class="line"></div>  <!-- Dividing line -->

            <div class="article" id="article1"> <!-- The new article tag. The id is supplied so it can be scrolled into view. -->
              <h2>Introduction</h2>

              <div class="line"></div>

              <div class="articleBody clear">

                <div class="figure"> <!-- The figure tag marks data (usually an image) that is part of the article -->
                  <a><img src="img/icon.png" width="581" height="328" alt="Image one" /></a>
                </div>
                <p>
                  Support the SQL query statement (select...from ...where....). The where clause allows the users to express simple predicates conditions connected by logical AND, OR, or NOT. Note this select statement has a minimal feature. Other features of the select statement are not required for the project.
                </p>
                <p>
                  The distributed database query engine shall run on three computers (sites) connected by a network. The architecture of the overall system must be P2P. For every site, the operating system is Windows, and the local DBMS is MySQL. The communication mechanism between sites can be socket, RPC, or others. The programming language for the distributed database query engine is open for every team to choose.
                </p>
              </div>
            </div>

            <!-- Article 1 end -->


            <!-- Article 2 start -->

            <div class="line"></div>

            <div class="article" id="article2">
              <h2>SQL-Online</h2>

              <div class="line"></div>

              <div class="articleBody clear">
                <div class="figure" id="result_div">
                  <!-- <a><img src="http://tutorialzine.com/img/featured/633.jpg" width="620" height="340" alt="Image two" /></a> -->
                  <div style="padding:10px">
                    <h3 style="color:yellow">Results:</h3>
                    <div id="results">
                      <h4 style="color:lightgray">Total Time:</h4><p id="totalTime"></p>
                      <h4 style="color:lightgray">Total Lines:</h4><p id="totalLines"></p>
                      <h4 style="color:lightgray">Transfer:</h4><p id="transfer"></p>
                    </div>
                  </div>
                </div>
                <p>Input a query like the following sentences:</p>
                <p  style="color:lightgray">
                  1)select * from Customer<br>
                  2)select publisher.name from Publisher<br>
                  3)select Book.title from Book where copies > 5000<br>
                  4)select customer_id, quantity from Orders where quantity < 8<br>
                </p>
                <textarea style="border: 2px dotted white;width:40%;padding:2px" rows=7 id="queryText"></textarea> 
                <button style="padding:1px;height:20px" id="sendButton">get result</button>
                <div id="QueryTree">
                  <div id="sidetree">
                    <div class="treeheader">&nbsp;</div>
                    <div id="sidetreecontrol"><a href="?#">Collapse All</a> | <a href="?#">Expand All</a></div>
                    <div id="replaceContent">
                      <ul id="tree">
                        <li><strong>project</strong>
                          <ul>
                            <li>tableB</li>
                            <li><strong>Join</strong>
                              <ul>
                                <li>tableC</li>
                                <li>tableD</li>
                              </ul>
                            </li>
                          </ul>
                        </li>
                      </ul> 
                    </div>
                  </div>

                </div>
              </div>
            </div>
          </div>

          <!-- Article 2 end -->

          <!-- Article 3 start -->

          <div class="line"></div>

          <div class="article" id="article3">
            <h2>Summary</h2>

            <div class="line"></div>

            <div class="articleBody clear">
              <div class="figure">
                <a><img src="img/design.png" width="620" height="340" alt="Image three" /></a>
              </div>
              <p>
                (1)由于数据量特别小,我们的算法基本上是基于内存的,为我们的系统带来了很大的限制,对于这一点,在将来的版本中,我们会有所改进。
              </p>
              <p>
                (2)首先在考虑冗余站点的时候,我们只是在生成执行计划前尽量优化的为查询子树分配站点号,没有做到实时动态的分配,这样如果一旦一个站点在执行执行计划的时候挂掉了,那么它就不能动态的转换到它的备份 站点, 浪费了备份站点的修复能力,这是我们需要改进的一个地方。
              </p>
            </div>
          </div>

          <!-- Article 3 end -->


        </div>

        <div class="footer"> <!-- Marking the footer section -->

          <div class="line"></div>

          <p>Department of Computer Science, Tsinghua University, Beijing, China</p> <!-- Change the copyright notice -->

          <a href="#" class="up">Go UP</a>
          <!-- <a href="http://tutorialzine.com/2010/02/free-xhtml-css3-website-template/" class="by">Template by Tutorialzine</a> -->


        </div>

      </div> <!-- Closing the #page section -->

      <!-- JavaScript Includes -->
      <script type="text/javascript" src="<%=path%>/jquery.min.js"></script>
      <script src="<%=path%>/script.js"></script>
      <script src="<%=path%>/jquery.scrollTo-1.4.2/jquery.scrollTo-min.js"></script>

      <script src="tree_js/jquery.js" type="text/javascript"></script>
      <script src="tree_js/jquery.cookie.js" type="text/javascript"></script>
      <script src="tree_js/jquery.treeview.js" type="text/javascript"></script>
      <script>
      $(document).ready(function() {

        $("#sendButton").click(function() {
          var txt = $("#queryText").val();
          $("#totalTime").toggle(200);
          $("#totalLines").toggle(200);
          $("#transfer").toggle(200);
          $.get("query",{"text":txt}, function(data){
            var s = data.split("!@#@!");
            $("#totalTime").text(s[0]);
            $("#totalLines").text(s[1]);
            $("#transfer").text(s[2]);
            $("#replaceContent").html(s[3]);
            $("#totalTime").toggle(800);
            $("#totalLines").toggle(800);
            $("#transfer").toggle(800);
            $("#tree").treeview({
              collapsed: true,
              animated: "fast",
              control:"#sidetreecontrol",
              persist: "location"
            });
          }); 
        });

        $.get("init",{}, function(data){
        });

        $("#tree").treeview({
          collapsed: true,
          animated: "fast",
          control:"#sidetreecontrol",
          persist: "location"
        });

      });
</script> 
</body>

</html>