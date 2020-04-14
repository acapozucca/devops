# 5- Evolve

You have reached an initial version of a deployment pipeline that covers the fundamental steps.
You have reached this state following an incremental approach. That means that, from now on, you have to work continuously to improve the deployment pipeline. 

Next, some points to be considered for improving the pipeline are given. 

* **Automate**: remove (or minimise) any yet required manual actions taking part during the setup of the pipeline's components. 

* **Cycle-time**: measure the time spent on each stage to see exactly how long does the entire pipeline takes to run. This allows you to find out the pipeline's bottlenecks and solve them in order of priority. Other metrics, beside cycle-time that can be used to assess the pipeline are reported in the article [15 Metrics for DevOps Success](https://stackify.com/15-metrics-for-devops-success/#post-14669-_jx3advhf4n5g)

* **Add stages**: add stages (or jobs to an already defined stage) to cover more actions that allows the process to increase both the velocity of the delivery process and the quality of the delivery product. For example, you could add a job into the build stage to do automated code analysis, or a new stage to test non-functional requirements as performance or capacity.  