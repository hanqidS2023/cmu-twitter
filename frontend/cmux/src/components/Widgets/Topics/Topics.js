import React from "react";
import "./Topics.css";
import TopicItem from "./TopicItem/TopicItem";
import { SettingsIcon } from "../../icons/index";

function Topics() {
  return (
    <div className="widgetsTopics">
      <div className="widgetsTopicsHeader">
        <span>Hot Courses</span>
        {/* <SettingsIcon /> */}
      </div>
      <TopicItem
        category="Course · Trending"
        title="SDA"
        numberoftweet="13.1K"
      />
      <TopicItem
        category="Course · Trending"
        title="Data Science"
        numberoftweet="13.1K"
      />
      <TopicItem
        category="Course · Trending"
        title="FSE"
        numberoftweet="13.1K"
      />
      <TopicItem
        category="Course · Trending"
        title="Testing"
        numberoftweet="13.1K"
      />
      <TopicItem
        category="Course · Trending"
        title="Cloud Computing"
        numberoftweet="13.1K"
      />

      {/* <div className="widgetsTopicMore">
        <span>Show more</span>
      </div> */}
    </div>
  );
}

export default Topics;
