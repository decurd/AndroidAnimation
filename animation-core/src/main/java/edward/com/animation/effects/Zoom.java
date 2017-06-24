/*
 * Copyright (C) 2015 YangEdward
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edward.com.animation.effects;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import edward.com.animation.evaluators.AccelerateEvaluator;
import edward.com.animation.evaluators.BaseEvaluator;
import edward.com.animation.evaluators.DecelerateEvaluator;
import edward.com.animation.evaluators.OvershootEvaluator;

import static edward.com.animation.effects.AnimPropertyName.ALPHA;
import static edward.com.animation.effects.AnimPropertyName.SCALE_X;
import static edward.com.animation.effects.AnimPropertyName.SCALE_Y;

public class Zoom extends EffectHasDirection{

    public Zoom(){
        super(null);
    }

    public Zoom(Action action) {
        super(null,action);
    }

    public Zoom(@NonNull Direction direction) {
        super(direction);
    }

    public Zoom(@NonNull Action action,@NonNull Direction direction){
        super(direction,action);
    }

    @Override
    public Animator[] getAnimators(@NonNull View target) {
        if (direction == null){
            return zoom(target);
        }
        return direction.getAnimators(target,this);
    }

    private Animator[] zoom(View target){
        return new Animator[]{
                new AnimatorBuilder(target,duration,action).setAnimator(ALPHA)
                        .getAnimator(),
                new AnimatorBuilder(target,duration,action).setAnimator(SCALE_X)
                        .getAnimator(),
                new AnimatorBuilder(target,duration,action).setAnimator(SCALE_Y)
                        .getAnimator()
        };
    }

    @Override
    public Animator[] top(View target) {
        isParentNull(target);
        float from = -parent.getHeight();
        float to = 0;
        switch (action){
            case IN:
                break;
            case OUT:
                to = from;
                from = 0;
                break;
        }
        return generate(target,from,to,AnimPropertyName.TRANSLATION_Y);
    }

    @Override
    public Animator[] left(View target) {
        isParentNull(target);
        float from = target.getLeft() - parent.getWidth();
        //float from = -target.getRight();
        float to = 0;
        switch (action){
            case IN:
                break;
            case OUT:
                to = from;
                from = 0;
                break;
        }
        return generate(target,from,to,AnimPropertyName.TRANSLATION_X);
    }

    @Override
    public Animator[] right(View target) {
        //isParentNull(target);
        //float from = target.getWidth() + target.getPaddingRight();
        isParentNull(target);
        float from = parent.getWidth() - target.getLeft();
        float to = 0;
        switch (action){
            case IN:
                break;
            case OUT:
                to = parent.getWidth() - parent.getLeft();
                from = 0;
                break;
        }
        return generate(target,from,to,AnimPropertyName.TRANSLATION_X);
    }

    @Override
    public Animator[] bottom(View target) {
        isParentNull(target);
        float from = parent.getHeight() + target.getTop();
        float to = 0;
        switch (action){
            case IN:
                break;
            case OUT:
                to = from;
                from = 0;
                break;
        }
        return generate(target,from,to,AnimPropertyName.TRANSLATION_Y);
    }

    private Animator[] generate(View target,float from,float to,AnimPropertyName value){
        evaluator = new DecelerateEvaluator();
        return new Animator[]{
                new AnimatorBuilder(target,duration,action).setAnimator(ALPHA)
                        .setEvaluator(evaluator)
                        .getAnimator(),
                new AnimatorBuilder(target,duration,action).setAnimator(SCALE_X,0.1f,1)
                        .setEvaluator(evaluator)
                        .getAnimator(),
                new AnimatorBuilder(target,duration,action).setAnimator(SCALE_Y,0.1f,1)
                        .setEvaluator(evaluator)
                        .getAnimator(),
                new AnimatorBuilder(target,duration,action).setAnimatorNoAction(value,from,to)
                        .setEvaluator(new OvershootEvaluator())
                        .getAnimator()
        };
    }
}
