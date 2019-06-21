<!DOCTYPE html>
<html>
<head>
    <title>qa</title>
    <style type="text/css">
        a:link {color: #e74c3c} /* 未访问的链接 */
        a:visited {color: #f1c40f} /* 已访问的链接 */
        a:hover {color: #2ecc71} /* 鼠标移动到链接上 */
        a:active {color: #9b59b6} /* 选定的链接 */
    </style>
</head>
<body bgcolor="white">
<#assign sel = "${sel}">
<#if (sel="qa")>
    ${value}
</#if>
<#if (sel="graph")>

    <script type="text/javascript" src="http://d3js.org/d3.v5.min.js"></script>

    <div style="position:absolute;left:10px; top:10px;z-index:-9999;"><p id = "info">text</p></div>

    <svg width="360" height="240"></svg>
    <script>
        var p = document.getElementById("info")

        var marge = {top:60,bottom:60,left:60,right:60}
        var svg = d3.select("svg")
        var width = svg.attr("width")
        var height = svg.attr("height")
        var g = svg.append("g")
            .attr("transform","translate("+marge.top+","+marge.left+")");

        //准备数据
        var nodes = [
            ${nodes}
        ];


        var edges = [
            ${links}
        ];


        //设置一个color的颜色比例尺，为了让不同的扇形呈现不同的颜色
        var colorScale = d3.scaleOrdinal()
            .domain(d3.range(nodes.length))
            .range(d3.schemeCategory10);

        //新建一个力导向图
        var forceSimulation = d3.forceSimulation()
            .force("link",d3.forceLink())
            .force("charge",d3.forceManyBody())
            .force("center",d3.forceCenter());

        //初始化力导向图，也就是传入数据
        //生成节点数据
        forceSimulation.nodes(nodes)
            .on("tick",ticked);//这个函数很重要，后面给出具体实现和说明
        //生成边数据
        forceSimulation.force("link")
            .links(edges)
            .distance(function(d){//每一边的长度
                return d.value*100;
            })
        //设置图形的中心位置
        forceSimulation.force("center")
            .x(width/2.5)
            .y(height/2.5);
        //在浏览器的控制台输出
        console.log(nodes);
        console.log(edges);

        //有了节点和边的数据后，我们开始绘制
        //绘制边
        var links = g.append("g")
            .selectAll("line")
            .data(edges)
            .enter()
            .append("line")
            .attr("stroke",function(d,i){
                return colorScale(i);
            })
            .attr("stroke-width",1);
        var linksText = g.append("g")
            .selectAll("text")
            .data(edges)
            .enter()
            .append("text")
            .text(function(d){
                return '';
            })

        //绘制节点
        //老规矩，先为节点和节点上的文字分组
        var gs = g.selectAll(".circleText")
            .data(nodes)
            .enter()
            .append("g")
            .attr("transform",function(d,i){
                var cirX = d.x;
                var cirY = d.y;
                return "translate("+cirX+","+cirY+")";
            })
            .call(d3.drag()
                .on("start",started)
                .on("drag",dragged)
                .on("end",ended)
            ).on("click",function(d){
                var str = "";
                excludeList = ["x","y","vx","vy","fx","fy","index"];
                for (var key in d){
                    if (excludeList.indexOf(key) != -1) {
                        continue;
                    }
                    str += key+" : "+d[key]+"\n";
                }
                p.innerText = str;
            });

        //绘制节点
        gs.append("circle")
            .attr("r",20)
            .attr("fill",function(d,i){
                return colorScale(i);
            })
        //文字
        gs.append("text")
            .attr("style","dominant-baseline:middle;text-anchor:middle;")
            .text(function(d){
                return d.name;
            })

        function ticked(){
            links
                .attr("x1",function(d){return d.source.x;})
                .attr("y1",function(d){return d.source.y;})
                .attr("x2",function(d){return d.target.x;})
                .attr("y2",function(d){return d.target.y;});

            linksText
                .attr("x",function(d){
                    return (d.source.x+d.target.x)/2;
                })
                .attr("y",function(d){
                    return (d.source.y+d.target.y)/2;
                });

            gs
                .attr("transform",function(d) { return "translate(" + d.x + "," + d.y + ")"; });
        }
        function started(d){
            if(!d3.event.active){
                forceSimulation.alphaTarget(0.8).restart();
            }
            d.fx = d.x;
            d.fy = d.y;
        }
        function dragged(d){
            d.fx = d3.event.x;
            d.fy = d3.event.y;
        }
        function ended(d){
            if(!d3.event.active){
                forceSimulation.alphaTarget(0);
            }
            d.fx = null;
            d.fy = null;
        }
    </script>
</#if>
<script>
    if("${sel}".length > 5)
        window.location.href="${sel}";
</script>

</body>
</html>
