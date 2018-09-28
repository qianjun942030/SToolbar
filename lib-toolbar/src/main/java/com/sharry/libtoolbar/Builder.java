package com.sharry.libtoolbar;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import static androidx.annotation.Dimension.DP;
import static androidx.annotation.Dimension.SP;
import static com.sharry.libtoolbar.Utils.dp2px;
import static com.sharry.libtoolbar.Utils.isNotEmpty;

/**
 * Build SToolbar more easy.
 *
 * @author Sharry <a href="frankchoochina@gmail.com">Contact me.</a>
 * @version 2.0
 * @since 2018/8/27 23:36
 */
public class Builder {

    private static final int INVALIDATE = -1;

    private Context mContext;
    private ViewGroup mContentParent;
    private Style mStyle = Style.DEFAULT;

    private int mBgColor = INVALIDATE;
    private int mBgDrawableResId = INVALIDATE;
    private int mMinimumHeight = INVALIDATE;
    private int mItemHorizontalInterval = INVALIDATE;

    private int mTitleGravity = Gravity.CENTER | Gravity.TOP;
    private List<Entity> mTitleEntities = new ArrayList<>();
    private List<Entity> mMenuLeftEntities = new ArrayList<>();
    private List<Entity> mMenuRightEntities = new ArrayList<>();

    private static class Entity {
        View view;
        Options op;

        public Entity(View view, Options op) {
            this.view = view;
            this.op = op;
        }
    }

    /**
     * 给 Activity 添加 Toolbar
     */
    Builder(Context context) {
        if (context instanceof Activity) {
            mContext = context;
            mContentParent = ((Activity) mContext).findViewById(Window.ID_ANDROID_CONTENT);
        } else {
            throw new IllegalArgumentException("Please ensure context instanceof Activity.");
        }
    }

    /**
     * 给 View 添加 Toolbar, 确保传入的 View 为 LinearLayout
     */
    Builder(View contentView) {
        if (contentView instanceof LinearLayout) {
            mContentParent = (ViewGroup) contentView;
            mContext = contentView.getContext();
        } else {
            throw new IllegalArgumentException("Please ensure parameter contentView instanceof " +
                    "LinearLayout, now is: " + contentView);
        }
    }

    /**
     * Set interval associated with this toolbar sub item.
     */
    public Builder setItemHorizontalInterval(@Dimension(unit = DP) int horizontalInterval) {
        mItemHorizontalInterval = horizontalInterval;
        return this;
    }

    /**
     * Set minimumHeight associated with this toolbar.
     */
    public Builder setMinimumHeight(@Dimension(unit = DP) int minimumHeight) {
        mMinimumHeight = minimumHeight;
        return this;
    }

    /**
     * Set style associated with bind activity status bar.
     */
    public Builder setStatusBarStyle(Style statusBarStyle) {
        mStyle = statusBarStyle;
        return this;
    }

    /**
     * Set the background color to a given resource. The colorResId should refer to
     * a color int.
     */
    public Builder setBackgroundColorRes(@ColorRes int colorResId) {
        mBgColor = ContextCompat.getColor(mContext, colorResId);
        return this;
    }

    /**
     * Set the background color associated with this toolbar.
     */
    public Builder setBackgroundColor(@ColorInt int color) {
        mBgColor = color;
        return this;
    }

    /**
     * Set the background to a given resource. The resource should refer to
     * a Drawable object or 0 to remove the background.
     */
    public Builder setBackgroundDrawableRes(@DrawableRes int drawableResId) {
        mBgDrawableResId = drawableResId;
        return this;
    }

    /**
     * Set gravity associated with this toolbar title.
     */
    public Builder setTitleGravity(int gravity) {
        mTitleGravity = gravity;
        return this;
    }

    /**
     * Set text associated with this toolbar title.
     */
    public Builder setTitleText(CharSequence text) {
        this.setTitleText(text, TextViewOptions.DEFAULT_TITLE_TEXT_SIZE);
        return this;
    }

    public Builder setTitleText(CharSequence text, @Dimension(unit = SP) int textSize) {
        this.setTitleText(text, textSize, TextViewOptions.DEFAULT_TEXT_COLOR);
        return this;
    }

    public Builder setTitleText(CharSequence text, @Dimension(unit = SP) int textSize, @ColorInt int textColor) {
        this.setTitleText(
                new TextViewOptions.Builder()
                        .setText(text)
                        .setTextSize(textSize)
                        .setTextColor(textColor)
                        .build()
        );
        return this;
    }

    public Builder setTitleText(@NonNull TextViewOptions options) {
        return setTitleView(null, options);
    }

    /**
     * Set image associated with this toolbar title.
     */
    public Builder setTitleImage(@DrawableRes int drawableRes) {
        this.setTitleImage(drawableRes, INVALIDATE, INVALIDATE);
        return this;
    }

    public Builder setTitleImage(@DrawableRes int drawableRes, @Dimension(unit = DP) int width,
                                 @Dimension(unit = DP) int height) {
        this.setTitleImage(
                new ImageViewOptions.Builder()
                        .setDrawableResId(drawableRes)
                        .setWidth(dp2px(mContext, width))
                        .setHeight(dp2px(mContext, height))
                        .build()
        );
        return this;
    }

    public Builder setTitleImage(@NonNull ImageViewOptions options) {
        return setTitleView(null, options);
    }

    /**
     * Set add custom view associated with this toolbar title.
     */
    public Builder setTitleView(@NonNull View view) {
        return setTitleView(view, null);
    }

    public Builder setTitleView(View view, Options options) {
        mTitleEntities.add(new Entity(view, options));
        return this;
    }

    /**
     * Add back icon associated with this toolbar left menu.
     */
    public Builder addBackIcon(@DrawableRes int drawableRes) {
        return addLeftMenuImage(
                new ImageViewOptions.Builder()
                        .setDrawableResId(drawableRes)
                        .setListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((Activity) mContext).finish();
                            }
                        })
                        .build()
        );
    }

    /**
     * Add sub item associated with this toolbar left menu.
     */
    public Builder addLeftMenuText(@NonNull TextViewOptions options) {
        return addLeftMenuView(null, options);
    }

    public Builder addLeftMenuImage(@NonNull ImageViewOptions options) {
        return addLeftMenuView(null, options);
    }

    public Builder addLeftMenuView(@NonNull View view) {
        return addLeftMenuView(view, null);
    }

    public Builder addLeftMenuView(@Nullable View view, @Nullable Options options) {
        mMenuLeftEntities.add(new Entity(view, options));
        return this;
    }

    /**
     * Add sub item associated with this toolbar right menu.
     */
    public Builder addRightMenuText(@NonNull TextViewOptions options) {
        return addRightMenuView(null, options);
    }

    public Builder addRightMenuImage(@NonNull ImageViewOptions options) {
        return addRightMenuView(null, options);
    }

    public Builder addRightMenuView(@NonNull View view) {
        return addRightMenuView(view, null);
    }

    public Builder addRightMenuView(@NonNull View view, @NonNull Options options) {
        mMenuRightEntities.add(new Entity(view, options));
        return this;
    }

    /**
     * Instantiation SToolbar.
     */
    public SToolbar build() {
        final SToolbar toolbar = new SToolbar(mContext);
        completion(toolbar);
        return toolbar;
    }

    /**
     * Instantiation SToolbar, and then add it to suitable position.
     */
    public SToolbar apply() {
        final SToolbar toolbar = build();
        // Add to container.
        mContentParent.addView(toolbar, 0);
        // 等待 View 的 performTraversal 完成
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                adjustLayout(toolbar);
            }
        });
        return toolbar;
    }

    /**
     * Inject data to toolbar.
     */
    private void completion(SToolbar toolbar) {
        // 1. Set layout params associated with the toolbar.
        toolbar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        // 2. Set arguments.
        if (INVALIDATE != mMinimumHeight) {
            toolbar.setMinimumHeight(dp2px(mContext, mMinimumHeight));
        }
        if (INVALIDATE != mItemHorizontalInterval) {
            toolbar.setSubItemInterval(dp2px(mContext, mItemHorizontalInterval));
        }
        if (Style.DEFAULT != mStyle) {
            toolbar.setStatusBarStyle(mStyle);
        }
        if (INVALIDATE != mBgColor) {
            toolbar.setBackgroundColor(mBgColor);
        }
        if (INVALIDATE != mBgDrawableResId) {
            toolbar.setBackgroundDrawableRes(mBgDrawableResId);
        }
        // 3. Setup title items associated with the toolbar.
        toolbar.setTitleGravity(mTitleGravity);
        if (isNotEmpty(mTitleEntities)) {
            for (Entity titleEntity : mTitleEntities) {
                if (null != titleEntity.view && null != titleEntity.op) {
                    toolbar.addTitle(titleEntity.view, titleEntity.op);
                } else if (null != titleEntity.op) {
                    if (titleEntity.op instanceof TextViewOptions) {
                        toolbar.setTitleText((TextViewOptions) titleEntity.op);
                    } else if (titleEntity.op instanceof ImageViewOptions) {
                        toolbar.setTitleImage((ImageViewOptions) titleEntity.op);
                    } else {
                        throw new NullPointerException("U setup options cannot support auto generate view, " +
                                " option is :" + titleEntity.op);
                    }
                } else if (null != titleEntity.view) {
                    toolbar.addTitle(titleEntity.view);
                } else {
                    throw new NullPointerException("Please ensure options mapper view nonnull");
                }
            }
        }
        // 4. Add left menu items associated with the toolbar.
        if (isNotEmpty(mMenuLeftEntities)) {
            for (Entity leftItem : mMenuLeftEntities) {
                if (null != leftItem.view && null != leftItem.op) {
                    toolbar.addLeftMenuView(leftItem.view, leftItem.op);
                } else if (null != leftItem.op) {
                    if (leftItem.op instanceof TextViewOptions) {
                        toolbar.addLeftMenuText((TextViewOptions) leftItem.op);
                    } else if (leftItem.op instanceof ImageViewOptions) {
                        toolbar.addLeftMenuImage((ImageViewOptions) leftItem.op);
                    } else {
                        throw new NullPointerException("U setup options cannot support auto generate view, " +
                                " option is :" + leftItem.op);
                    }
                } else if (null != leftItem.view) {
                    toolbar.addLeftMenuView(leftItem.view);
                } else {
                    throw new NullPointerException("Please ensure options mapper view nonnull");
                }
            }
        }
        // 5. Add right menu items associated with the toolbar.
        if (isNotEmpty(mMenuRightEntities)) {
            for (Entity rightEntity : mMenuRightEntities) {
                if (null != rightEntity.view && null != rightEntity.op) {
                    toolbar.addRightMenuView(rightEntity.view, rightEntity.op);
                } else if (null != rightEntity.op) {
                    if (rightEntity.op instanceof TextViewOptions) {
                        toolbar.addRightMenuText((TextViewOptions) rightEntity.op);
                    } else if (rightEntity.op instanceof ImageViewOptions) {
                        toolbar.addRightMenuImage((ImageViewOptions) rightEntity.op);
                    } else {
                        throw new NullPointerException("U setup options cannot support auto generate view, " +
                                " option is :" + rightEntity.op);
                    }
                } else if (null != rightEntity.view) {
                    toolbar.addRightMenuView(rightEntity.view);
                } else {
                    throw new NullPointerException("Please ensure options mapper view nonnull");
                }
            }
        }
    }

    /**
     * Adjust origin content to comfort position.
     */
    private void adjustLayout(SToolbar toolbar) {
        if (null != mContentParent && !(mContentParent instanceof LinearLayout)) {
            // Move origin content under the SToolbar.
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                    mContentParent.getChildAt(1).getLayoutParams();
            params.topMargin += toolbar.getHeight();
            mContentParent.getChildAt(1).setLayoutParams(params);
        }
    }

}
